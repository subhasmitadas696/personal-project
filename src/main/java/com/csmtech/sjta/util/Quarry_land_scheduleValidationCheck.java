package com.csmtech.sjta.util;

import org.json.JSONObject;
public class Quarry_land_scheduleValidationCheck {
public static String BackendValidation(JSONObject obj) {
String errMsg=null;
Integer errorStatus = 0;
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtQuarryAgreementNo").toString())){
 errorStatus = 1;
errMsg= "Quarry Agreement No should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtQuarryAgreementNo"),1)) {
errorStatus = 1;
errMsg= "Quarry Agreement No  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtQuarryAgreementNo"),45)) {
errorStatus = 1;
errMsg= "Quarry Agreement No maxmimum length should be 45";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtQuarryAgreementNo"))){
 errorStatus = 1;
errMsg= "Quarry Agreement Noshould be SpecialCharKey !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selDistrict").toString())){
 errorStatus = 1;
errMsg= "District  should not be empty !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selTehsil").toString())){
 errorStatus = 1;
errMsg= "Tehsil  should not be empty !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selMouza").toString())){
 errorStatus = 1;
errMsg= "Mouza  should not be empty !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selKhataNo").toString())){
 errorStatus = 1;
errMsg= "Khata No.  should not be empty !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selPlotNo").toString())){
 errorStatus = 1;
errMsg= "Plot No.  should not be empty !";}
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtAreainAcre").toString())){
 errorStatus = 1;
errMsg= "Area (in Acre) should not  be empty!";}
if (errorStatus == 0 && CommonValidator.validateFile((String)obj.get("fileEnvironmentClearance"))){
 errorStatus = 1;
errMsg= "Invalid File Type !";}
if (errorStatus == 0 && CommonValidator.validateFile((String)obj.get("fileConsenttoEstablish"))){
 errorStatus = 1;
errMsg= "Invalid File Type !";}
if (errorStatus == 0 && CommonValidator.validateFile((String)obj.get("fileCTEApprovalfromPollutionControlBoard"))){
 errorStatus = 1;
errMsg= "Invalid File Type !";}
if (errorStatus == 0 && CommonValidator.validateFile((String)obj.get("fileCTOConsenttoOperate"))){
 errorStatus = 1;
errMsg= "Invalid File Type !";}
if (errorStatus == 0 && CommonValidator.validateFile((String)obj.get("fileAgreementCopy"))){
 errorStatus = 1;
errMsg= "Invalid File Type !";}
return errMsg;
}
}