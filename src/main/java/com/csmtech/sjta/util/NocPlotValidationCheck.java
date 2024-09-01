package com.csmtech.sjta.util;

import org.json.JSONObject;
public class NocPlotValidationCheck {
public static String BackendValidation(JSONObject obj) {
String errMsg=null;
Integer errorStatus = 0;
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selDistrictName").toString())){
 errorStatus = 1;
errMsg= "District Name  should not be empty !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selTehsilName").toString())){
 errorStatus = 1;
errMsg= "Tehsil Name  should not be empty !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selMouza").toString())){
 errorStatus = 1;
errMsg= "Mouza  should not be empty !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selKhataNo").toString())){
 errorStatus = 1;
errMsg= "Khata No.  should not be empty !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selPlotNo").toString())){
 errorStatus = 1;
errMsg= "Plot No.  should not be empty !";}
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtTotalRakba").toString())){
 errorStatus = 1;
errMsg= "Total Rakba should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtTotalRakba"),1)) {
errorStatus = 1;
errMsg= "Total Rakba  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtTotalRakba"),45)) {
errorStatus = 1;
errMsg= "Total Rakba maxmimum length should be 45";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtTotalRakba"))){
 errorStatus = 1;
errMsg= "Total Rakbashould be SpecialCharKey !";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtPurchaseRakba"),1)) {
errorStatus = 1;
errMsg= "Purchase Rakba  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtPurchaseRakba"),45)) {
errorStatus = 1;
errMsg= "Purchase Rakba maxmimum length should be 45";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtPurchaseRakba"))){
 errorStatus = 1;
errMsg= "Purchase Rakbashould be SpecialCharKey !";}
if (errorStatus == 0 && CommonValidator.validateFile((String)obj.get("fileDocumentaryProofofOccupancyifany"))){
 errorStatus = 1;
errMsg= "Invalid File Type !";}
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtFixedPriceperAcreofPurchasedLand").toString())){
 errorStatus = 1;
errMsg= "Fixed Price per Acre of Purchased Land should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtFixedPriceperAcreofPurchasedLand"),1)) {
errorStatus = 1;
errMsg= "Fixed Price per Acre of Purchased Land  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtFixedPriceperAcreofPurchasedLand"),90)) {
errorStatus = 1;
errMsg= "Fixed Price per Acre of Purchased Land maxmimum length should be 90";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtFixedPriceperAcreofPurchasedLand"))){
 errorStatus = 1;
errMsg= "Fixed Price per Acre of Purchased Landshould be SpecialCharKey !";}
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtTotalCostofLandPurchased").toString())){
 errorStatus = 1;
errMsg= "Total Cost of Land Purchased should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtTotalCostofLandPurchased"),1)) {
errorStatus = 1;
errMsg= "Total Cost of Land Purchased  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtTotalCostofLandPurchased"),90)) {
errorStatus = 1;
errMsg= "Total Cost of Land Purchased maxmimum length should be 90";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtTotalCostofLandPurchased"))){
 errorStatus = 1;
errMsg= "Total Cost of Land Purchasedshould be SpecialCharKey !";}
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtOthers").toString())){
 errorStatus = 1;
errMsg= "Others should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtOthers"),1)) {
errorStatus = 1;
errMsg= "Others  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtOthers"),450)) {
errorStatus = 1;
errMsg= "Others maxmimum length should be 450";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtOthers"))){
 errorStatus = 1;
errMsg= "Othersshould be SpecialCharKey !";}
return errMsg;
}
}