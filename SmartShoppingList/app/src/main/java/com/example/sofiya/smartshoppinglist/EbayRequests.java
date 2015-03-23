package com.example.sofiya.smartshoppinglist;

public class EbayRequests {

//    http://www.developer.ebay.com/DevZone/finding/CallRef/findItemsByKeywords.html#Request.keywords

    public static final String sSearchByKeywordUrl = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsByKeywords&SERVICE-VERSION=1.12.0&SECURITY-APPNAME=SofiyaTa-ce9c-4caf-9267-359dc21130ef&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&keywords=";
    public static final String sSearchByCategoryUrl = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsByCategory&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=SofiyaTa-ce9c-4caf-9267-359dc21130ef&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&categoryId=";
    public static final String sElectronicsCategoryId = "293";
    public static final String sCollectiblesCategoryId = "1";
    public static final String sFashionCategoryId = "11450";
    public static final String sHomeCategoryId = "11700";
    public static final String sCampingCategoryId = "16034";
    public static final String sMotorsCategoryId = "6000";
    public static final String sPaginationUrl = "&paginationInput.entriesPerPage=";
    public static final String sPageNumberUrl = "&paginationInput.pageNumber=";


}
