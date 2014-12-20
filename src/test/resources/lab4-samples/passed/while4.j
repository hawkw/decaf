.class public while4
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
	LoopBegin610227087:
		LoopBegin850765774:
			.line 7
				getstatic		java/lang/System/out Ljava/io/PrintStream;
			ldc		0x1
				invokevirtual		java/io/PrintStream/print(I)V

			.line 8
			goto		End850765774

			.line 9
				getstatic		java/lang/System/out Ljava/io/PrintStream;
			ldc		0x2
				invokevirtual		java/io/PrintStream/print(I)V

			.line 10
			return
			ldc		0x1
			ldc		0x1
			if_icmpeq		LoopBegin850765774
		End850765774:

		.line 12
			getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		0x3
			invokevirtual		java/io/PrintStream/print(I)V

		.line 13
		goto		End610227087

		.line 14
			getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		0x4
			invokevirtual		java/io/PrintStream/print(I)V

		.line 15
		return
		ldc		0x1
		ldc		0x1
		if_icmpeq		LoopBegin610227087
	End610227087:

	.line 17
		getstatic		java/lang/System/out Ljava/io/PrintStream;
	ldc		0x5
		invokevirtual		java/io/PrintStream/print(I)V

EndGlobal:
return
.end method

