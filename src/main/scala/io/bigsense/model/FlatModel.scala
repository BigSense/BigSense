package io.bigsense.model

case class FlatModel(
  headers : List[String],
  cols : List[String],
  rows : List[Map[String,Any]]
) extends ModelTrait