package com.market.main;
import javax.swing.*;
public class Welcome {

	public static void main(String[] args) {
		 try {
	            // 님버스 테마 적용
	            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

	            // Nimbus 색상(배경 포함) 흰색으로 재설정
	            UIManager.put("control", java.awt.Color.WHITE);
	            UIManager.put("info", java.awt.Color.WHITE);
	            UIManager.put("nimbusBase", java.awt.Color.WHITE);
	            UIManager.put("nimbusLightBackground", java.awt.Color.WHITE);
	            UIManager.put("nimbusSelectionBackground", java.awt.Color.LIGHT_GRAY);
	            UIManager.put("text", java.awt.Color.BLACK);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

		new GuestWindow("고객 정보 입력", 0, 0, 1000, 750);
	}
}