$(document).ready(function() {
	
 const commaPrice = function() {
  const prices = document.querySelectorAll('[id^="renewPrice"]');
  for (var i = 0; i < prices.length; i++) {
    const price = parseInt(prices[i].innerHTML);
    const formattedPrice = price.toLocaleString();
    prices[i].innerHTML = formattedPrice;
  }
};
  
  $('.noStock').hide();
  
  $('.titlebox:first').show();
  
  // 나머지 리스트 요소를 숨김
  $('.titlebox:not(:first)').hide();

  commaPrice();
  //function hideButton() {
  //const products = document.getElementsByClassName('titlebox');
  
  // 각 상품에 대해 재고 여부를 확인하고 선택 버튼을 숨깁니다.
  //for (var i = 0; i < products.length; i++) {
    //const product = products[i];
    
    //if (!hasStock(product)) {
      //const selectionButton = product.getElementsByClassName('selection-button')[0];
      //selectionButton.style.display = 'none';
    //}
  //}
//}
  
  
  const directStock = $('#directStock').val();
  console.log(directStock);
  if(!directStock == 0 && directStock !== null){
	  ;
  }
 
 
  
  
 // Option 선택시 directCode 완성된 것을 $('#directCode').val에 저장
  $('.optionArea').on('click', 'li[name="colorCode"]', function() { // 컬러 선택시
    const selectedOptions = getSelectedOptions();

    // 추가작업
    let colorCode = selectedOptions.colorCode;
    let categoryCode = $("#categoryCode").val();
    let brandCode = $("#brandCode").val();
    let slicedCode = $("#slicedCode").val();
    let directCode = '';
    if (colorCode != null) {
      directCode = "P" + categoryCode + "B" + brandCode + "C" + colorCode + "V000" + slicedCode;
      $('#directCode').text(directCode);
      updateTitleBoxVisibility(directCode); // 일치하는 titlebox 업데이트
      
      const colorValue=$('#colorName').val('C'+colorCode).val();
      if(colorValue==="CW"){
		  const colorName = "화이트";
		  $('#colorName').val(colorName);
	  }else if(colorValue==="CB"){
		  const colorName = "블랙";
		  $('#colorName').val(colorName);
	  }else if(colorValue==="CG"){
		  const colorName = "그레이";
		  $('#colorName').val(colorName);
	  }else{
		  const colorName ="옵션없음";
		  $('#colorName').val(colorName);
	  }
      
    }


  // 일치하는 directCode를 가진 titlebox를 표시하고 나머지는 숨김
  function updateTitleBoxVisibility(directCode) {
    const directListItems = $('#directList').find('.direct-item');
    let matchingItemFound = false;
    directListItems.each(function() {
      const listItem = $(this);
      const listItemDirectCode = listItem.attr('data-direct-code');

      if (listItemDirectCode === directCode) {
        listItem.closest('.titlebox').show(); 
        matchingItemFound = true;  
      } else {
        listItem.closest('.titlebox').hide();
      }
      
    });
  if (!matchingItemFound) {
	  
    $('.noStock').show(); // 일치하는 제품이 없는 경우 재고없음을 나타내는 div를 보여줌
  } else {
    $('.noStock').hide(); // 일치하는 제품이 있는 경우 재고없음을 나타내는 div를 숨김
  }  


  }


function getSelectedOptions() {
  var options = {
    colorCode: null,
  };

  var colorCode = $('.optionArea li[name="colorCode"].selected').attr('value');
  

  if (colorCode) {
    options.colorCode = colorCode;
  }
  return options;
	}
}); 

$(".quantity-wrapper .quantity-right-plus").on("click", function() {
  var $qty = $(this).siblings(".input-wrapper").find(".input-number");
  var currentVal = parseInt($qty.val(), 10);
  if (!isNaN(currentVal)) {
    $qty.val(currentVal + 1);
    updateTotalPrice(); // 수량이 변경될 때 총 가격 업데이트
  }
});

$(".quantity-wrapper .quantity-left-minus").on("click", function() {
  var $qty = $(this).siblings(".input-wrapper").find(".input-number");
  var currentVal = parseInt($qty.val(), 10);
  if (!isNaN(currentVal) && currentVal > 1) {
    $qty.val(currentVal - 1);
    updateTotalPrice(); // 수량이 변경될 때 총 가격 업데이트
  }
});

function updateTotalPrice() {
  const qty = parseInt($(".input-number").val(), 10);
  const price = parseInt($("#renewPrice").text().replace(",", ""), 10);
  const totalPrice = qty * price;
  	$("#subscriptionPrice").text(totalPrice.toLocaleString()); // 총 가격 업데이트
	$('#totalPrice').val(totalPrice);
	$('#orderAmount').val(qty);
	
}

  // 후기 작성 버튼 클릭 시 모달 창이 열릴 때 실행되는 함수
  $('#addReview').on('show.bs.modal', function (event) {
    const directName = $('.directNameValue').data('direct-name'); // 해당 후기 작성 버튼에 연결된 제품의 directName 값을 가져옴
    $('#directName2').text(directName); // 모달 창 내에서 제품명을 표시하는 곳에 directName 값을 설정
  });
  //문의 작성 버튼 클릭 시 모달 창이 열릴 때 실행되는 함수
  $('#addQna').on('show.bs.modal', function (event) {
	    const directName = $('.directNameValue').data('direct-name'); // 해당 후기 작성 버튼에 연결된 제품의 directName 값을 가져옴
	    $('#directName3').text(directName); // 모달 창 내에서 제품명을 표시하는 곳에 directName 값을 설정
	  });
  
  $('#addReply').on('show.bs.modal', function(event) {
	  const button = $(event.relatedTarget); // 클릭한 버튼 요소
	  const qnaNum = button.data('qna-num'); // data-qna-num 속성 값 가져오기
	  const qnaContents = button.closest('.accordion-item').find('#qnaContentsQ').text().trim(); // 문의 내용 가져오기
	  const directName = $('.directNameValue').data('direct-name'); // 해당 후기 작성 버튼에 연결된 제품의 directName 값을 가져옴
	    $('#directName4').text(directName); // 모달 창 내에서 제품명을 표시하는 곳에 directName 값을 설정

	  // 모달 내부 요소에 값 설정
	  $(this).find('#modalQnaNum').val(qnaNum);
	  $(this).find('#modelQnaContents').val(qnaContents);
	});

  });