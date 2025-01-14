package com.mycoffee.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mycoffee.domain.ProductJoinVO;
import com.mycoffee.domain.Product_CategoryVO;

public interface ProductMapper {
	public Product_CategoryVO get(String category);
	public ProductJoinVO get2(@Param("category")String category,@Param("tem")int tem, @Param("cap")int cap);
	public ProductJoinVO get3(String pid);
	
	
	
	/**
	 * 현제 시판중인 음료 혹은 음식 카테고리 목록을 검색
	 * @param tpype 0:drink, 1:food
	 * @return
	 */
	public List<Product_CategoryVO> getlist(int tpype);
	//public List<Product_CategoryVO> getlist1(String category);
	
	/**
	 * 저정한 카테고리에 등록된 상품중 현제 시판중인 목록을 검색
	 * @param category
	 * @return
	 */
	public List<ProductJoinVO> getlist1(String category);
	public int getcount();
}
