.class public if5
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

.limit locals 2
.limit stack 5
BeginGlobal:
.var 0 is i I from BeginGlobal to EndGlobal
	ldc		0x0
	istore		0

	.line 6
						iload		0
						ldc		0xc
						if_icmpgt	Cmp1745805329
						ldc		0x0
						goto		CmpDone1745805329
					Cmp1745805329:
						ldc		0x1
					CmpDone1745805329:
						iload		0
						ldc		0xc
						if_icmplt	Cmp732502651
						ldc		0x0
						goto		CmpDone732502651
					Cmp732502651:
						ldc		0x1
					CmpDone732502651:
					ior
					ldc		0x3
					ldc		0x5
					if_icmpeq	Cmp1223275358
					ldc		0x0
					goto		CmpDone1223275358
				Cmp1223275358:
					ldc		0x1
				CmpDone1223275358:
				ior
			ldc		0x1
			iand
		ldc		0x1
		if_icmpne		IfNot8122930
	.line 6
		.line 7
			getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		0x1
			invokevirtual		java/io/PrintStream/print(I)V
	IfNot8122930:

	.line 9
		getstatic		java/lang/System/out Ljava/io/PrintStream;
	ldc		0x2
		invokevirtual		java/io/PrintStream/print(I)V

EndGlobal:
return
.end method

