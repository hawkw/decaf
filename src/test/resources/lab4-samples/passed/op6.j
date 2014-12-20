.class public op6
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
		if_icmpgt	Cmp287882774
		ldc		0x0
		goto		CmpDone287882774
	Cmp287882774:
		ldc		0x1
	CmpDone287882774:
		invokevirtual		java/io/PrintStream/print(Z)V

	.line 3
		getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		0x1
		ldc		0x0
		if_icmpgt	Cmp1435573326
		ldc		0x0
		goto		CmpDone1435573326
	Cmp1435573326:
		ldc		0x1
	CmpDone1435573326:
		invokevirtual		java/io/PrintStream/print(Z)V

	.line 4
		getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		0x1
		ldc		0x1
		if_icmpgt	Cmp784972134
		ldc		0x0
		goto		CmpDone784972134
	Cmp784972134:
		ldc		0x1
	CmpDone784972134:
		invokevirtual		java/io/PrintStream/print(Z)V

EndGlobal:
return
.end method

