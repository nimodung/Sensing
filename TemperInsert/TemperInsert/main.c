/*
 * TemperInsert.c
 *
 * Created: 2019-07-10 오후 4:41:56
 * Author : tjoeun
 */ 

#include <avr/io.h>
#include <util/delay.h>
#include <avr/interrupt.h>
#define F_CPU 16000000UL

#include "DHT11.h"
#include "Timer.h"
#include "Uart.h"

extern volatile char check_DHT11_flag, sec, min, hour;
extern volatile int time_msec;

int main(void)
{
	//char cmd_idx = 0;
	TemperInsertInit();
    while (1) 
    {
		
		if(check_DHT11_flag) {
			
			check_DHT11_flag = 0;
			check_DHT11();
		}
    }
}

void TemperInsertInit(void) {
	Timer0_init();
	UART0_init(9600);
	DHT11_init();
	sei();
	DDRB |= 1 << PORTB5;
	PORTB &= ~(1 << PORTB5);
	return;
}



void check_DHT11() {
	char RH_integral, RH_decimal, Tmpr_integral, Tmpr_decimal;
	
	
	DHT11_trigger(); //트리거
	
	data_input(); //입력 전환
	
	dumi_read(); // 데이터 전송 전에 시간 흘려보내기
	
	
	RH_integral = rx_byte(); //데이터 값 저장
	RH_decimal  = rx_byte();
	Tmpr_integral = rx_byte();
	Tmpr_decimal = rx_byte();
	
	rx_byte(); //check_sum
	
	//DHT11_init();
	data_output(); // data pin 출력 전환
	
	//printf("\n", );
	printf("time %d %d %d temper %d.%d humi %d.%d\n",hour, min, sec, Tmpr_integral, Tmpr_decimal, RH_integral, RH_decimal);
	//printf("\n", );
	
	// _delay_ms(1500); //적당한 시간을 주지않으면 초기화를 못해서 한번만 출력하고 안된당
	//delay_flag = 1;
	
	return;
	
	
}