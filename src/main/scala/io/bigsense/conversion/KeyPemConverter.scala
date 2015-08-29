package io.bigsense.conversion

import java.security.KeyPair
import java.sql.Clob

import io.bigsense.spring.MySpring

import scala.collection.mutable

/**
 * Created by cassius on 29/08/15.
 */
class KeyPemConverter extends ConverterTrait {

  private def keyString(key : String) = {
    MySpring.commandLineSignatureManager.keyPairFromPem(key) match {
      case Some(pair : KeyPair) => {
        if(pair.getPrivate != null) {
          "Private"
        }
        else if(pair.getPublic != null) {
          "Public"
        }
        else {
          "Invalid"
        }
      }
      case None => "None"
    }
  }

  override def convertRow(row: mutable.Map[String, Any], arg: String) = {

    row("key_pem") = row("key_pem") match {
      case clob:Clob => keyString(clob.getSubString(1,clob.length().asInstanceOf[Int]))
      //MySQL returns TEXT fields as Strings
      case str:String => keyString(str)
      case null => "None"
    }
  }
}

