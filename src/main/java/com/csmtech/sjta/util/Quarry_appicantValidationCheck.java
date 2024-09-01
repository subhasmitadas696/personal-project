package com.csmtech.sjta.util;

import org.json.JSONObject;
public class Quarry_appicantValidationCheck {
public static String BackendValidation(JSONObject obj) {
String errMsg=null;
Integer errorStatus = 0;
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtQuarryName").toString())){
 errorStatus = 1;
errMsg= "Quarry Name should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtQuarryName"),1)) {
errorStatus = 1;
errMsg= "Quarry Name  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtQuarryName"),50)) {
errorStatus = 1;
errMsg= "Quarry Name maxmimum length should be 50";}
if (errorStatus == 0 &&CommonValidator.isCharecterKey((String) obj.get("txtQuarryName"))){
 errorStatus = 1;
errMsg= "Quarry Name should be a character!";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtQuarryName"))){
 errorStatus = 1;
errMsg= "Quarry Nameshould be SpecialCharKey !";}
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtPropatoryName").toString())){
 errorStatus = 1;
errMsg= "Propatory Name should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtPropatoryName"),1)) {
errorStatus = 1;
errMsg= "Propatory Name  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtPropatoryName"),45)) {
errorStatus = 1;
errMsg= "Propatory Name maxmimum length should be 45";}
if (errorStatus == 0 &&CommonValidator.isCharecterKey((String) obj.get("txtPropatoryName"))){
 errorStatus = 1;
errMsg= "Propatory Name should be a character!";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtPropatoryName"))){
 errorStatus = 1;
errMsg= "Propatory Nameshould be SpecialCharKey !";}
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtFatherHusbandName").toString())){
 errorStatus = 1;
errMsg= "Father/Husband Name should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtFatherHusbandName"),1)) {
errorStatus = 1;
errMsg= "Father/Husband Name  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtFatherHusbandName"),45)) {
errorStatus = 1;
errMsg= "Father/Husband Name maxmimum length should be 45";}
if (errorStatus == 0 &&CommonValidator.isCharecterKey((String) obj.get("txtFatherHusbandName"))){
 errorStatus = 1;
errMsg= "Father/Husband Name should be a character!";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtFatherHusbandName"))){
 errorStatus = 1;
errMsg= "Father/Husband Nameshould be SpecialCharKey !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selDocumentType").toString())){
 errorStatus = 1;
errMsg= "Document Type  should not be empty !";}
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtDocumentRefNo").toString())){
 errorStatus = 1;
errMsg= "Document Ref. No. should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtDocumentRefNo"),1)) {
errorStatus = 1;
errMsg= "Document Ref. No.  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtDocumentRefNo"),45)) {
errorStatus = 1;
errMsg= "Document Ref. No. maxmimum length should be 45";}
if (errorStatus == 0 &&CommonValidator.isAlphaNumericKey((String) obj.get("txtDocumentRefNo"))){
 errorStatus = 1;
errMsg= "Document Ref. No. should be AlphaNumeric !";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtDocumentRefNo"))){
 errorStatus = 1;
errMsg= "Document Ref. No.should be SpecialCharKey !";}
if (errorStatus == 0 && CommonValidator.isEmpty((String)obj.get("fileDocument"))){
 errorStatus = 1;
errMsg= "Document file should not be empty !";}
if (errorStatus == 0 && CommonValidator.validateFile((String)obj.get("fileDocument"))){
 errorStatus = 1;
errMsg= "Invalid File Type !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selState").toString())){
 errorStatus = 1;
errMsg= "State  should not be empty !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selDistrict").toString())){
 errorStatus = 1;
errMsg= "District  should not be empty !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selBlockULB").toString())){
 errorStatus = 1;
errMsg= "Block/ULB  should not be empty !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selGPWardNumber").toString())){
 errorStatus = 1;
errMsg= "GP/Ward Number  should not be empty !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selVillageLocalAreaName").toString())){
 errorStatus = 1;
errMsg= "Village/Local Area Name  should not be empty !";}
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtPoliceStation").toString())){
 errorStatus = 1;
errMsg= "Police Station should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtPoliceStation"),1)) {
errorStatus = 1;
errMsg= "Police Station  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtPoliceStation"),45)) {
errorStatus = 1;
errMsg= "Police Station maxmimum length should be 45";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtPoliceStation"))){
 errorStatus = 1;
errMsg= "Police Stationshould be SpecialCharKey !";}
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtPostOffice").toString())){
 errorStatus = 1;
errMsg= "Post Office should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtPostOffice"),1)) {
errorStatus = 1;
errMsg= "Post Office  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtPostOffice"),45)) {
errorStatus = 1;
errMsg= "Post Office maxmimum length should be 45";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtPostOffice"))){
 errorStatus = 1;
errMsg= "Post Officeshould be SpecialCharKey !";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtHabitationStreetLandmark"),1)) {
errorStatus = 1;
errMsg= "Habitation/Street/Landmark  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtHabitationStreetLandmark"),90)) {
errorStatus = 1;
errMsg= "Habitation/Street/Landmark maxmimum length should be 90";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtHabitationStreetLandmark"))){
 errorStatus = 1;
errMsg= "Habitation/Street/Landmarkshould be SpecialCharKey !";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtHouseNumberOtherDetails"),1)) {
errorStatus = 1;
errMsg= "House Number/Other Details  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtHouseNumberOtherDetails"),45)) {
errorStatus = 1;
errMsg= "House Number/Other Details maxmimum length should be 45";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtHouseNumberOtherDetails"))){
 errorStatus = 1;
errMsg= "House Number/Other Detailsshould be SpecialCharKey !";}
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtPincode").toString())){
 errorStatus = 1;
errMsg= "Pincode should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtPincode"),6)) {
errorStatus = 1;
errMsg= "Pincode  minimum length should be 6";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtPincode"),6)) {
errorStatus = 1;
errMsg= "Pincode maxmimum length should be 6";}
if (errorStatus == 0 &&CommonValidator.isNumericKey((String) obj.get("txtPincode"))){
 errorStatus = 1;
errMsg= "Pincode should be Numeric!";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtPincode"))){
 errorStatus = 1;
errMsg= "Pincodeshould be SpecialCharKey !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selState16").toString())){
 errorStatus = 1;
errMsg= "State  should not be empty !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selDistrict17").toString())){
 errorStatus = 1;
errMsg= "District  should not be empty !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selBlockULB18").toString())){
 errorStatus = 1;
errMsg= "Block/ULB  should not be empty !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selGPWardNumber19").toString())){
 errorStatus = 1;
errMsg= "GP/Ward Number  should not be empty !";}
if (errorStatus == 0 && CommonValidator.blankCheckRdoDropChk(obj.get("selVillageLocalAreaName20").toString())){
 errorStatus = 1;
errMsg= "Village/Local Area Name  should not be empty !";}
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtPoliceStation21").toString())){
 errorStatus = 1;
errMsg= "Police Station should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtPoliceStation21"),1)) {
errorStatus = 1;
errMsg= "Police Station  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtPoliceStation21"),50)) {
errorStatus = 1;
errMsg= "Police Station maxmimum length should be 50";}
if (errorStatus == 0 &&CommonValidator.isCharecterKey((String) obj.get("txtPoliceStation21"))){
 errorStatus = 1;
errMsg= "Police Station should be a character!";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtPoliceStation21"))){
 errorStatus = 1;
errMsg= "Police Stationshould be SpecialCharKey !";}
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtPostOffice22").toString())){
 errorStatus = 1;
errMsg= "Post Office should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtPostOffice22"),1)) {
errorStatus = 1;
errMsg= "Post Office  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtPostOffice22"),50)) {
errorStatus = 1;
errMsg= "Post Office maxmimum length should be 50";}
if (errorStatus == 0 &&CommonValidator.isCharecterKey((String) obj.get("txtPostOffice22"))){
 errorStatus = 1;
errMsg= "Post Office should be a character!";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtPostOffice22"))){
 errorStatus = 1;
errMsg= "Post Officeshould be SpecialCharKey !";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtHabitationStreetLandmark23"),1)) {
errorStatus = 1;
errMsg= "Habitation/Street/Landmark  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtHabitationStreetLandmark23"),90)) {
errorStatus = 1;
errMsg= "Habitation/Street/Landmark maxmimum length should be 90";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtHabitationStreetLandmark23"))){
 errorStatus = 1;
errMsg= "Habitation/Street/Landmarkshould be SpecialCharKey !";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtHouseNumberOtherDetails24"),1)) {
errorStatus = 1;
errMsg= "House Number/Other Details  minimum length should be 1";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtHouseNumberOtherDetails24"),45)) {
errorStatus = 1;
errMsg= "House Number/Other Details maxmimum length should be 45";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtHouseNumberOtherDetails24"))){
 errorStatus = 1;
errMsg= "House Number/Other Detailsshould be SpecialCharKey !";}
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtPincode25").toString())){
 errorStatus = 1;
errMsg= "Pincode should not  be empty!";}
 if (errorStatus == 0 && CommonValidator.minLengthCheck((String) obj.get("txtPincode25"),6)) {
errorStatus = 1;
errMsg= "Pincode  minimum length should be 6";}
 if (errorStatus == 0 && CommonValidator.maxLengthCheck((String) obj.get("txtPincode25"),6)) {
errorStatus = 1;
errMsg= "Pincode maxmimum length should be 6";}
if (errorStatus == 0 &&CommonValidator.isNumericKey((String) obj.get("txtPincode25"))){
 errorStatus = 1;
errMsg= "Pincode should be Numeric!";}
if (errorStatus == 0 &&CommonValidator.isSpecialCharKey((String) obj.get("txtPincode25"))){
 errorStatus = 1;
errMsg= "Pincodeshould be SpecialCharKey !";}
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtDurationStartDate").toString())){
 errorStatus = 1;
errMsg= "Duration Start Date should not  be empty!";}
if (errorStatus == 0 &&CommonValidator.isEmpty( obj.get("txtDurationEndDate").toString())){
 errorStatus = 1;
errMsg= "Duration End Date should not  be empty!";}
return errMsg;
}
}