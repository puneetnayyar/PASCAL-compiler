	#Program description
	#@author Puneet Nayyar
	#@version 5/3/18
	

	.data
count:
	.word 0
ignore:
	.word 0
times:
	.word 0
newline:
	.asciiz "\n"
	.text
	.globl main
main:
	li $v0, 196			#assign value of 196 to $v0
	sw $v0, count		#assign value in $v0 to count
	li $v0, 0			#assign value of 0 to $v0
	sw $v0, times		#assign value in $v0 to times
	li $v0, 10			#assign value of 10 to $v0
	subu $sp, $sp, 4	#subtract 4 from the stack pointer
	sw $v0, ($sp)		#push value of $v0 onto the stack
	li $v0, 13			#assign value of 13 to $v0
	subu $sp, $sp, 4	#subtract 4 from the stack pointer
	sw $v0, ($sp)		#push value of $v0 onto the stack
	jal procprintSquares		#jump and link to procprintSquares
	sw $v0, ignore		#assign value in $v0 to ignore
	la $t0, count		#load address of count into $t0
	lw $v0, ($t0)		#load value of address in $t0 to $v0
	move $a0, $v0		#load value of $v0 into $a0
	li $v0, 1			#print value in $a0
	syscall
	la $a0, newline		#load new line character into $a0
	li $v0, 4			#print new line
	syscall
	la $t0, times		#load address of times into $t0
	lw $v0, ($t0)		#load value of address in $t0 to $v0
	move $a0, $v0		#load value of $v0 into $a0
	li $v0, 1			#print value in $a0
	syscall
	la $a0, newline		#load new line character into $a0
	li $v0, 4			#print new line
	syscall
	li $v0, 10			#standard termination
	syscall
procprintSquares:
	li $v0, 0		#load 0 into $v0
	subu $sp, $sp, 4	#subtract 4 from the stack pointer
	sw $v0, ($sp)		#push value of $v0 onto the stack
	li $t0, 0		#load 0 into $t0
	subu $sp, $sp, 4	#subtract 4 from the stack pointer
	sw $t0, ($sp)		#push value of $t0 onto the stack
	li $t0, 0		#load 0 into $t0
	subu $sp, $sp, 4	#subtract 4 from the stack pointer
	sw $t0, ($sp)		#push value of $t0 onto the stack
	subu $sp, $sp, 4	#subtract 4 from the stack pointer
	sw $ra, ($sp)		#push value of $ra onto the stack
	la $t0, 20($sp)		#load address of low into $t0
	lw $v0, ($t0)		#load value of address in $t0 to $v0
	sw $v0, 8($sp)		#assign value in $v0 to count
while1:
	la $t0, 8($sp)		#load address of count into $t0
	lw $v0, ($t0)		#load value of address in $t0 to $v0
	subu $sp, $sp, 4	#subtract 4 from the stack pointer
	sw $v0, ($sp)		#push value of $v0 onto the stack
	la $t0, 20($sp)		#load address of high into $t0
	lw $v0, ($t0)		#load value of address in $t0 to $v0
	lw $t0, ($sp)		#pop the stack and place value into $t0
	addu $sp, $sp, 4	#add 4 to the stack pointer
	bgt $t0, $v0, endif1#if $t0 is greater than $v0, jump to endif1
	la $t0, 8($sp)		#load address of count into $t0
	lw $v0, ($t0)		#load value of address in $t0 to $v0
	subu $sp, $sp, 4	#subtract 4 from the stack pointer
	sw $v0, ($sp)		#push value of $v0 onto the stack
	la $t0, 12($sp)		#load address of count into $t0
	lw $v0, ($t0)		#load value of address in $t0 to $v0
	lw $t0, ($sp)		#pop the stack and place value into $t0
	addu $sp, $sp, 4	#add 4 to the stack pointer
	mult $t0, $v0		#multiply $t0 and $v0
	mflo $v0			#put product into $v0
	sw $v0, 4($sp)		#assign value in $v0 to square
	la $t0, 4($sp)		#load address of square into $t0
	lw $v0, ($t0)		#load value of address in $t0 to $v0
	move $a0, $v0		#load value of $v0 into $a0
	li $v0, 1			#print value in $a0
	syscall
	la $a0, newline		#load new line character into $a0
	li $v0, 4			#print new line
	syscall
	la $t0, 8($sp)		#load address of count into $t0
	lw $v0, ($t0)		#load value of address in $t0 to $v0
	subu $sp, $sp, 4	#subtract 4 from the stack pointer
	sw $v0, ($sp)		#push value of $v0 onto the stack
	li $v0, 1			#assign value of 1 to $v0
	lw $t0, ($sp)		#pop the stack and place value into $t0
	addu $sp, $sp, 4	#add 4 to the stack pointer
	addu $v0, $t0, $v0	#add $t0 and $v0 and put sum into $v0
	sw $v0, 8($sp)		#assign value in $v0 to count
	la $t0, times		#load address of times into $t0
	lw $v0, ($t0)		#load value of address in $t0 to $v0
	subu $sp, $sp, 4	#subtract 4 from the stack pointer
	sw $v0, ($sp)		#push value of $v0 onto the stack
	li $v0, 1			#assign value of 1 to $v0
	lw $t0, ($sp)		#pop the stack and place value into $t0
	addu $sp, $sp, 4	#add 4 to the stack pointer
	addu $v0, $t0, $v0	#add $t0 and $v0 and put sum into $v0
	sw $v0, times		#assign value in $v0 to times
	j while1			#jump to while1
endif1:
	lw $ra, ($sp)		#pop the stack and place value into $ra
	addu $sp, $sp, 4	#add 4 to the stack pointer
	lw $t0, ($sp)		#pop the stack and place value into $t0
	addu $sp, $sp, 4	#add 4 to the stack pointer
	lw $t0, ($sp)		#pop the stack and place value into $t0
	addu $sp, $sp, 4	#add 4 to the stack pointer
	lw $v0, ($sp)		#pop the stack and place value into $v0
	addu $sp, $sp, 4	#add 4 to the stack pointer
	jr $ra				#return to address in $ra
