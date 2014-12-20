.class public for6
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

.limit locals 3
.limit stack 5
BeginGlobal:
.var 0 is i I from BeginGlobal to EndGlobal

.var 1 is j I from BeginGlobal to EndGlobal
		ldc		0x2
		istore		0
	LoopBegin918468867:
			ldc		0x2
			istore		1
		LoopBegin591746869:
			.line 7
						iload		0
						iload		1
						imul
					ldc		0xA
					if_icmpeq	CmpEQ1420882517
					ldc		0x0
					goto		CmpEQDone1420882517
				CmpEQ1420882517:
					ldc		0x1
				CmpEQDone1420882517:
				ldc		0x1
				if_icmpne		IfNot1803903626
			.line 7
				.line 7
				goto		End591746869
			IfNot1803903626:

			.line 8
				getstatic		java/lang/System/out Ljava/io/PrintStream;
				iload		0
				invokevirtual		java/io/PrintStream/print(I)V
				getstatic		java/lang/System/out Ljava/io/PrintStream;
				iload		1
				invokevirtual		java/io/PrintStream/print(I)V
				getstatic		java/lang/System/out Ljava/io/PrintStream;
				ldc		"\n"
				invokevirtual		java/io/PrintStream/print(Ljava/lang/String;)V

				iload		1
				ldc		0x1
				iadd
			istore		1
				iload		1
				ldc		0x6
				if_icmplt	CmpLT2126209824
				ldc		0x0
				goto		CmpLTDone2126209824
			CmpLT2126209824:
				ldc		0x1
			CmpLTDone2126209824:
			ldc		0x1
			if_icmpeq		LoopBegin591746869
		End591746869:

		.line 10
				iload		0
				ldc		0x5
				if_icmpeq	CmpEQ1306686609
				ldc		0x0
				goto		CmpEQDone1306686609
			CmpEQ1306686609:
				ldc		0x1
			CmpEQDone1306686609:
			ldc		0x1
			if_icmpne		IfNot185796610
		.line 10
			.line 10
			goto		End918468867
		IfNot185796610:
			iload		0
			ldc		0x1
			iadd
		istore		0
			iload		0
			ldc		0x7
			if_icmplt	CmpLT755127563
			ldc		0x0
			goto		CmpLTDone755127563
		CmpLT755127563:
			ldc		0x1
		CmpLTDone755127563:
		ldc		0x1
		if_icmpeq		LoopBegin918468867
	End918468867:

	.line 12
		getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		"done"
		invokevirtual		java/io/PrintStream/print(Ljava/lang/String;)V


	.line 13
		getstatic		java/lang/System/out Ljava/io/PrintStream;
		iload		0
		invokevirtual		java/io/PrintStream/print(I)V
		getstatic		java/lang/System/out Ljava/io/PrintStream;
		iload		1
		invokevirtual		java/io/PrintStream/print(I)V


EndGlobal:
return
.end method

