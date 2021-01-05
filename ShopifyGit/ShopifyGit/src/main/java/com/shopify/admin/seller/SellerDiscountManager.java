package com.shopify.admin.seller;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.shopify.common.util.UtilFunc;
import com.shopify.order.popup.OrderCourierData;

public class SellerDiscountManager {
	
	private List<OrderCourierData> list;
//	private boolean[] changed;				// list 가 elment 에 변동이 있으면 true 로 설정한다.
	
	public SellerDiscountManager(List<OrderCourierData> list) {
		super();
		this.list = list;
	}

	public String processData(OrderCourierData data) {
		String result = null;
		
		
		// 1. 사용자가 입력한 data 의 startDate 이  list에 이미 들어있으면 startDate 이 동일하면, discount 값만 udpate 하고, 없으면 추가한다.
		// 1-a. start 일자가 동일한 것이 있는 것만 별도의 list 로  만들어서
		List<OrderCourierData> collect = this.list.stream().filter(d -> d.getStartDate().equals(data.getStartDate())).collect(Collectors.toList());
		
		if ( collect.size() > 0 ) {
			// 1-2. 
			for( OrderCourierData already : collect ) {
				if ( already.getDiscount() != data.getDiscount() ) {
					already.setDiscount(data.getDiscount());
					already.setProc("Y");
				}
			}
		} else {
			this.list.add(data);
			this.list.sort( Comparator.comparing(OrderCourierData::getStartDate) );
		}
		
		// 3. start Date 오류 찾기
		List<String> dates = this.list.stream().map(OrderCourierData::getStartDate).collect(Collectors.toList());
		if ( validateDate(dates) == false ) {
			result = "시작일자에 겹치는 날짜가 있습니다. " + String.join(", ", dates);
			return result;
		}
		
		
		// 4. 이전 record 의 end 날짜를 start date 하루 전으로 수정한다.
		String holdEndDate = null;;
		OrderCourierData record = null;
		for( int idx = list.size() - 1; idx >= 0; idx-- ) {
			if ( idx == list.size() - 1 ) {
				holdEndDate =  "9999-12-31";
			} else {
				holdEndDate = UtilFunc.calcDays(record.getStartDate(), -1);
			}
			
			record = list.get(idx);
			
			if ( holdEndDate.equals(record.getEndDate() ) == false ) {
				record.setProc("Y");
				record.setEndDate(holdEndDate);
			}
		}
		
		// 4. end Date 오류 찾기
		dates = this.list.stream().map(OrderCourierData::getEndDate).collect(Collectors.toList());
		if ( validateDate(dates) == false ) {
			result = "종료일자에 겹치는 날짜가 있습니다. "  + String.join(", ", dates);;
			return result;
		}
		
		// 5. start date 가  end Date 이후의 날짜이면 에러
		List<OrderCourierData> wrongList = this.list.stream().filter( d -> d.getStartDate().compareTo(d.getEndDate()) > 0 ).collect(Collectors.toList());
		if ( wrongList.size() > 0 ) {
			StringBuilder buffer = new StringBuilder();
			buffer.append("시작일자와 종료일자가 잘못되었습니다." );
			for( OrderCourierData d : wrongList ) {
				buffer.append( AdminSellerService.printData(d));
				buffer.append("\n");
			}
			return buffer.toString();
		}
		
		return result;
	}

	private boolean validateDate(List<String> list) {
		boolean result = true;
		
		String holdDate = "0";
		for( String date : list ) {
			if ( date.compareTo(holdDate) <= 0 ) {
				result = false;
				break;
			}
		}
		
		return result;		
	}

	public List<OrderCourierData> getChangedRecords() {
		// list data 중에서 변경된 것만 return한다.
		return this.list.stream().filter( d -> "Y".equals( d.getProc() ) ).collect(Collectors.toList());
	}


}
