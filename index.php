<?php 
$keyword=$price_from=$price_to=$cond=$buy=$return1=$free=$sortby=$res=$request=$days=$exp=$xml="";
$i=0;
$a=$b=0;
$number;
$count=0;
$URL="http://svcs.eBay.com/services/search/FindingService/v1?siteid=0&SECURITY-APPNAME=USC4cef84-390b-478e-8c79-5e4041fbd81&OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&RESPONSE-DATA-FORMAT=XML&keywords=";


class MainClass
{
  public $ack="";
  public $resultCount="";
  public $pageNumber="";
  public $itemCount="";
  public $item1=array();
}

class itemClass
{
  public $basicInfo;
  public $sellerInfo;
  public $shippingInfo;

}

class basicInfoClass
{
  public $title="";
  public $viewItemURL="";
  public $galleryURL="";
  public $pictureURLSuperSize="";
  public $convertedCurrentPrice="";
  public $shippingServiceCost="";
  public $conditionDisplayName="";
  public $listingType="";
  public $location="";
  public $categoryName="";
  public $topRatedListing="";

}

class sellerInfoClass
{
  public $sellerUserName;
  public $feedbackScore;	
  public $positiveFeedbackPercent;
  public $feedbackRatingStar;
  public $topRatedSeller;
  public $sellerStoreName;
  public $sellerStoreURL;
}

class shippingInfoClass
{
  public $shippingType;
  public $shipToLocations;
  public $expeditedShipping;
  public $oneDayShippingAvailable;
  public $returnsAccepted;
  public $handlingTime;
}

   
     $keyword= urlencode (utf8_encode ($_GET["keyword"]));
	 $keyword1=$_GET["keyword"];
       $request=$URL.$keyword;  
   
    
   if(!empty($_GET["price_from"]))
       {
         $price_from=$_GET["price_from"];   
          $request.="&itemFilter($i).name=MinPrice&itemFilter($i).value=".$price_from;
          $i=$i+1; 
          
       }
           
    if(!empty($_GET["price_to"]))
    {
       $price_to=$_GET["price_to"];
       $request.="&itemFilter($i).name=MaxPrice&itemFilter($i).value=".$price_to;
        $i=$i+1; 
         
    }
    if(isset($_GET["condition"]))
    {
       $cond=$_GET["condition"];
       $request.="&itemFilter($i).name=Condition";
       $b=count($cond);
       for($a=0;$a<$b;$a++){
           $request.="&itemFilter($i).value($a)=".$cond[$a];
        }
        $i=$i+1; 
    }
    
    if(isset($_GET["buy"]))
    {
     $buy=$_GET["buy"];
     $request.="&itemFilter($i).name=ListingType";
     $b=count($buy);
     for($a=0;$a<$b;$a++){
        $request.="&itemFilter($i).value($a)=".$buy[$a];
        }
        $i=$i+1;     
    }
    
    if(isset($_GET["return_accepted"])){
        
        $return1="true";
        $request.="&itemFilter($i).name=ReturnsAcceptedOnly&itemFilter($i).value=".$return1;
     $i=$i+1;   
    }
       
    
    if(isset($_GET["free_shipping"])){
        
        $free="true";
        $request.="&itemFilter($i).name=FreeShippingOnly&itemFilter($i).value=".$free;
      $i=$i+1;  
    }
      
    if(isset($_GET["expedited"])){
        $exp=$_GET["expedited"];
        $request.="&itemFilter($i).name=ExpeditedShippingType&itemFilter($i).value=Expedited";
     $i=$i+1;   
    }
       
    
    if(!empty($_GET["max_days"])){
        $days=$_GET["max_days"];
        $request.="&itemFilter($i).name=MaxHandlingTime&itemFilter($i).value=".$days;
    $i=$i+1;
    }
     
    
    $sortby=$_GET["sort_by"];
    $request.="&sortOrder=".$sortby;
  
  
    $request.="&paginationInput.entriesPerPage=5";
	    $request.="&outputSelector[0]="."SellerInfo";
    $request.="&outputSelector[1]="."PictureURLSuperSize";
    $request.="&outputSelector[2]="."StoreInfo";
   
    
   
     
    $xml=simplexml_load_file($request);
    $Main=new MainClass();
	 if((string)$xml->ack == "Failure" || (string)$xml->paginationOutput->totalEntries == "0"  )
    {
       
        $json=array();
        $json["ack"]="No results found";
    
        echo json_encode($json);
    }
   else if((string)$xml->ack == "Success")
   {
	   $Main->ack=(string)$xml->ack;
     $Main->resultCount=(string)$xml->paginationOutput->totalEntries;
    $Main->pageNumber=(string)$xml->paginationOutput->pageNumber;
    $Main->itemCount=(string)$xml->paginationOutput->entriesPerPage;
    foreach ($xml->searchResult->children() as $childnode){
        $item=new itemClass();
        $item->basicInfo=new basicInfoClass();
        $item->sellerInfo=new sellerInfoClass();
        $item->shippingInfo=new shippingInfoClass();
        
        $item->basicInfo->title=(string)$childnode->title;
        $item->basicInfo->viewItemURL=(string)$childnode->viewItemURL;
        $item->basicInfo->galleryURL=(string)$childnode->galleryURL;
        $item->basicInfo->pictureURLSuperSize=(string)$childnode->pictureURLSuperSize;
        $item->basicInfo->convertedCurrentPrice=(string)$childnode->sellingStatus->convertedCurrentPrice;
        $item->basicInfo->shippingServiceCost=(string)$childnode->shippingInfo->shippingServiceCost;
        $item->basicInfo->conditionDisplayName=(string)$childnode->condition->conditionDisplayName;
        $item->basicInfo->listingType=(string)$childnode->listingInfo->listingType;
        $item->basicInfo->location=(string)$childnode->location;
        $item->basicInfo->categoryName=(string)$childnode->primaryCategory->categoryName;
        $item->basicInfo->topRatedListing=(string)$childnode->topRatedListing;
            
       
         $item->sellerInfo->sellerUserName=(string)$childnode->sellerInfo->sellerUserName;
         $item->sellerInfo->feedbackScore=(string)$childnode->sellerInfo->feedbackScore;
         $item->sellerInfo->positiveFeedbackPercent=(string)$childnode->sellerInfo->positiveFeedbackPercent;
         $item->sellerInfo->feedbackRatingStar=(string)$childnode->sellerInfo->feedbackRatingStar;
         $item->sellerInfo->topRatedSeller=(string)$childnode->sellerInfo->topRatedSeller;
         $item->sellerInfo->sellerStoreName=(string)$childnode->storeInfo->storeName;
         $item->sellerInfo->sellerStoreURL=(string)$childnode->storeInfo->storeURL;
            
            
        $item->shippingInfo->shippingType=(string)$childnode->shippingInfo->shippingType;
         foreach($childnode->shippingInfo->children() as $shipnode){
        
           if($shipnode->getName()=="shipToLocations"){
               
            $item->shippingInfo->shipToLocations =$item->shippingInfo->shipToLocations.$shipnode.",";   
        
        }
        }
        $item->shippingInfo->shipToLocations=substr($item->shippingInfo->shipToLocations,0,-1);
        $item->shippingInfo->expeditedShipping=(string)$childnode->shippingInfo->expeditedShipping;
      
        $item->shippingInfo->oneDayShippingAvailable=(string)$childnode->shippingInfo->oneDayShippingAvailable;    
        
       
        $item->shippingInfo->returnsAccepted=(string)$childnode->returnsAccepted;  
        $item->shippingInfo->handlingTime=(string)$childnode->shippingInfo->handlingTime;      
        
        
        $Main->item1["item".$count]=$item;
        $count=$count+1;
            

        }
 
 echo json_encode($Main);
   }
    

?>