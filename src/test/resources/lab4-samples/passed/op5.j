.class public op5
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
		if_icmplt	Cmp-1530565894
		ldc		0x0
		goto		CmpDone-1530565894
	Cmp-1530565894:
		ldc		0x1
	CmpDone-1530565894:
		invokevirtual		java/io/PrintStream/print(Z)V

	.line 3
		getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		0x1
		ldc		0x0
		if_icmplt	Cmp2146476521
		ldc		0x0
		goto		CmpDone2146476521
	Cmp2146476521:
		ldc		0x1
	CmpDone2146476521:
		invokevirtual		java/io/PrintStream/print(Z)V

	.line 4
		getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		0x0
		ldc		0x0
		if_icmplt	Cmp-101127021
		ldc		0x0
		goto		CmpDone-101127021
	Cmp-101127021:
		ldc		0x1
	CmpDone-101127021:
		invokevirtual		java/io/PrintStream/print(Z)V

EndGlobal:
return
.end method

