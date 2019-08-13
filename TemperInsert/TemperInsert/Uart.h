/*
 * Uart.h
 *
 * Created: 2019-04-17 오전 9:49:14
 *  Author: user
 */ 


#ifndef UART_H_
#define UART_H_

#include <stdio.h>

int Uart_main(void);
void TX0_char(char data);
void UART0_init(unsigned long baud);
void TX0_string(char *string);
void TX0_4Digit(int data);


#endif /* UART_H_ */