.class public for4
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
	LoopBegin1622192341:
		.line 5
			getstatic		java/lang/System/out Ljava/io/PrintStream;
		iload		0
			invokevirtual		java/io/PrintStream/print(I)V

		.line 6
				iload		0
				ldc		0x3
				if_icmpeq	Cmp2048995625
				ldc		0x0
				goto		CmpDone2048995625
			Cmp2048995625:
				ldc		0x1
			CmpDone2048995625:
			ldc		0x1
			if_icmpne		IfNot1169765529
		.line 6
			.line 6
			goto		End1622192341
		IfNot1169765529:

		.line 7
			getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		" ok\n"
			invokevirtual		java/io/PrintStream/print(Ljava/lang/String;)V
			iload		0
			ldc		0x1
			iadd
		istore		0
			iload		0
			ldc		0x5
			if_icmplt	Cmp785919378
			ldc		0x0
			goto		CmpDone785919378
		Cmp785919378:
			ldc		0x1
		CmpDone785919378:
		ldc		0x1
		if_icmpeq		LoopBegin1622192341
	End1622192341:

	.line 9
		getstatic		java/lang/System/out Ljava/io/PrintStream;
	ldc		"done"
		invokevirtual		java/io/PrintStream/print(Ljava/lang/String;)V

EndGlobal:
return
.end method

