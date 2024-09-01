package com.csmtech.sjta.util;

import org.json.JSONObject;
public class NocDocumentsValidationCheck {
public static String BackendValidation(JSONObject obj) {
String errMsg=null;
Integer errorStatus = 0;
if (errorStatus == 0 && CommonValidator.isEmpty((String)obj.get("fileHalPatta"))){
 errorStatus = 1;
errMsg= "Hal Patta file should not be empty !";}
if (errorStatus == 0 && CommonValidator.validateFile((String)obj.get("fileHalPatta"))){
 errorStatus = 1;
errMsg= "Invalid File Type !";}
if (errorStatus == 0 && CommonValidator.validateFile((String)obj.get("fileSabikPatta"))){
 errorStatus = 1;
errMsg= "Invalid File Type !";}
if (errorStatus == 0 && CommonValidator.validateFile((String)obj.get("fileSabikHalComparisonStatement"))){
 errorStatus = 1;
errMsg= "Invalid File Type !";}
if (errorStatus == 0 && CommonValidator.validateFile((String)obj.get("fileSettlementYaddast"))){
 errorStatus = 1;
errMsg= "Invalid File Type !";}
if (errorStatus == 0 && CommonValidator.isEmpty((String)obj.get("fileRegisteredDeed"))){
 errorStatus = 1;
errMsg= "Registered Deed file should not be empty !";}
if (errorStatus == 0 && CommonValidator.validateFile((String)obj.get("fileRegisteredDeed"))){
 errorStatus = 1;
errMsg= "Invalid File Type !";}
return errMsg;
}
}