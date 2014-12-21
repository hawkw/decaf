.class public for5
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
	LoopBegin886896028:
			ldc		0x2
			istore		1
		LoopBegin1188812094:
			.line 7
						iload		0
						iload		1
						imul
					ldc		0xA
					if_icmpeq	CmpEQ1348311531
					ldc		0x0
					goto		CmpEQDone1348311531
				CmpEQ1348311531:
					ldc		0x1
				CmpEQDone1348311531:
				ldc		0x1
				if_icmpne		IfNot1084390935
			.line 7
				.line 7
				goto		End1188812094
			IfNot1084390935:

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
				if_icmplt	CmpLT2108339025
				ldc		0x0
				goto		CmpLTDone2108339025
			CmpLT2108339025:
				ldc		0x1
			CmpLTDone2108339025:
			ldc		0x1
			if_icmpeq		LoopBegin1188812094
		End1188812094:
			iload		0
			ldc		0x1
			iadd
		istore		0
			iload		0
			ldc		0x7
			if_icmplt	CmpLT1779352877
			ldc		0x0
			goto		CmpLTDone1779352877
		CmpLT1779352877:
			ldc		0x1
		CmpLTDone1779352877:
		ldc		0x1
		if_icmpeq		LoopBegin886896028
	End886896028:

	.line 11
		getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		"done"
		invokevirtual		java/io/PrintStream/print(Ljava/lang/String;)V


EndGlobal:
return
.end method

