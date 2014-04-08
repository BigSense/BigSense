package io.bigsense.processor


case class ProcessorException(message: String) extends RuntimeException(message) {}