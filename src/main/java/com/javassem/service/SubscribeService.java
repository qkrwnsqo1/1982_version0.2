package com.javassem.service;

import java.util.HashMap;
import java.util.List;

import com.javassem.domain.SubscribeVO;

public interface SubscribeService {
	int countList();
	List<SubscribeVO> getSubscribeList(HashMap map);
	List<SubscribeVO> getTotalList();
	int getType5();
	int getType4();
	int getType3();
	int getType2();
	int getType1();
	
}
