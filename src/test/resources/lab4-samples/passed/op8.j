.class public op8
.super java/lang/Object
;
; standard initializer (calls java.lang.Object's initializer)
;
.method public <init>()V
aload_0
invokenonvirtual java/lang/Object/<init>()V
return
.end method

.method public static main([Ljava/lang/String;)V

.limit locals 1
.limit stack 5
BeginGlobal:
	.line 2
		getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		0x0
		ldc		0x1
		if_icmpge	Cmp201007060
		ldc		0x0
		goto		CmpDone201007060
	Cmp201007060:
		ldc		0x1
	CmpDone201007060:
		invokevirtual		java/io/PrintStream/print(Z)V

	.line 3
		getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		0x0
		ldc		0x0
		if_icmpge	Cmp6893011
		ldc		0x0
		goto		CmpDone6893011
	Cmp6893011:
		ldc		0x1
	CmpDone6893011:
		invokevirtual		java/io/PrintStream/print(Z)V

	.line 4
		getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		0x1
		ldc		0x0
		if_icmpge	Cmp1586503089
		ldc		0x0
		goto		CmpDone1586503089
	Cmp1586503089:
		ldc		0x1
	CmpDone1586503089:
		invokevirtual		java/io/PrintStream/print(Z)V

EndGlobal:
return
.end method

