package io.bigsense.model

class FlatModel extends ModelTrait {

  var headers : List[String] = List()
  var rows : List[Map[String,Any]] = List()
  var cols : List[String] = List()
}