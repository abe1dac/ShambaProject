package com.arnold.myapplication.ml

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.TensorOperator
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.MappedByteBuffer

class Classifier(context: Context) {

    private var interpreter: Interpreter? = null
    private var inputImageWidth: Int = 0
    private var inputImageHeight: Int = 0
    private var labels: List<String> = emptyList()

    init {
        try {
            val tfliteModel: MappedByteBuffer = FileUtil.loadMappedFile(context, MODEL_PATH)
            val options = Interpreter.Options()
            options.setNumThreads(NUM_THREADS)
            interpreter = Interpreter(tfliteModel, options)
            labels = FileUtil.loadLabels(context, LABEL_PATH)

            val inputShape = interpreter?.getInputTensor(0)?.shape() ?: intArrayOf(1, 224, 224, 3)
            inputImageWidth = inputShape[1]
            inputImageHeight = inputShape[2]

        } catch (e: IOException) {
            Log.e(TAG, "Error initializing classifier.", e)
        }
    }

    fun classify(bitmap: Bitmap): List<Result> {
        if (interpreter == null) {
            Log.e(TAG, "Classifier not initialized.")
            return emptyList()
        }

        val imageTensor = loadImage(bitmap)
        val outputTensor = processOutputTensor()
        interpreter?.run(imageTensor.buffer, outputTensor.buffer)

        return getResults(outputTensor)
    }

    private fun loadImage(bitmap: Bitmap): TensorImage {
        // Load bitmap into tensor
        val imageTensor = TensorImage(inputDataType)
        imageTensor.load(bitmap)

        //Process image to be used on model.
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeWithCropOrPadOp(inputImageHeight, inputImageWidth))
            .add(ResizeOp(inputImageHeight, inputImageWidth, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(getPreprocessNormalizeOp())
            .build()
        return imageProcessor.process(imageTensor)
    }

    private fun processOutputTensor(): TensorBuffer {
        // Process the output of the model
        val probabilityTensor = interpreter?.getOutputTensor(0)
        val outputShape = probabilityTensor?.shape() ?: intArrayOf(1, labels.size)
        return TensorBuffer.createFixedSize(outputShape, outputDataType)
    }

    private fun getResults(outputTensor: TensorBuffer): List<Result> {
        // Get the model output
        val output = TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build()
            .process(outputTensor).floatArray
        val result = mutableListOf<Result>()
        // Add the results and its score
        for (i in labels.indices) {
            result.add(Result(labels[i], output[i]))
        }
        // Order the results by the highest score
        result.sortByDescending { it.confidence }
        return result
    }

    private fun getPreprocessNormalizeOp(): TensorOperator {
        // Normalize the image input
        return NormalizeOp(IMAGE_MEAN, IMAGE_STD)
    }

    private fun getPostprocessNormalizeOp(): TensorOperator {
        // Normalize the output.
        return NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD)
    }

    companion object {
        private const val TAG = "Classifier"
        private const val MODEL_PATH = "model_unquant.tflite"
        private const val LABEL_PATH = "labels.txt"
        private const val NUM_THREADS = 4

        private const val IMAGE_MEAN = 0.0f
        private const val IMAGE_STD = 1.0f

        private const val PROBABILITY_MEAN = 0.0f
        private const val PROBABILITY_STD = 1.0f

        private val inputDataType = DataType.FLOAT32
        private val outputDataType = DataType.FLOAT32
    }

    data class Result(val title: String, val confidence: Float)
}