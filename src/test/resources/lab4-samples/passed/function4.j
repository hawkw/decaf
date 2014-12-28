.source function4.decaf
.class public function4
.super java/lang/Object
;
; standard initializer (calls java.lang.Object's initializer)
;
.method public <init>()V
aload_0
invokenonvirtual java/lang/Object/<init>()V
return
.end method
.method public static readInt()I
	.limit stack 5
	.limit locals 1
	.line 4
ReadIntBegin:
	new	java/io/BufferedReader
	dup
	new	java/io/InputStreamReader
	dup
	getstatic	java/lang/System/in Ljava/io/InputStream;
	invokespecial	java/io/InputStreamReader/<init>(Ljava/io/InputStream;)V
	invokespecial	java/io/BufferedReader/<init>(Ljava/io/Reader;)V
	invokevirtual	java/io/BufferedReader/readLine()Ljava/lang/String;
	invokestatic	java/lang/Integer/parseInt(Ljava/lang/String;)I
ReadIntReturn:
	ireturn
ReadIntCatch:
	astore_0
	iconst_0
	ireturn
.catch java/lang/Exception from ReadIntBegin to ReadIntReturn using ReadIntCatch
.end method

.method public static readLine()Ljava/lang/String;
	.limit stack 5
.limit locals 1
ReadLineBegin:
	new java/io/BufferedReader
	dup
	new	java/io/InputStreamReader
	dup
	getstatic	java/lang/System/in Ljava/io/InputStream;
	invokespecial	java/io/InputStreamReader/<init>(Ljava/io/InputStream;)V
	invokespecial	java/io/BufferedReader/<init>(Ljava/io/Reader;)V
	invokevirtual	java/io/BufferedReader/readLine()Ljava/lang/String;
ReadLineReturn:
	areturn
ReadLineCatch:
	astore_0
	ldc	"Fail"
	areturn
.catch	java/lang/Exception from ReadLineBegin to ReadLineReturn using ReadLineCatch
.end method  

.method public static SayHi()V

.limit locals 1
.limit stack 5
BeginGlobal:
.var 0 is c I from BeginGlobal to EndGlobal
	ldc	0xF
	istore	0

	.line 5
		ldc	0x0
		ldc	0x1
		if_icmpne	IfNot1169612028
	.line 6
		.line 6
		return
	IfNot1169612028:

		iload	0
			iload	0
			iload	0
			imul
		isub
	istore	0

	.line 8
		getstatic	java/lang/System/out Ljava/io/PrintStream;
			ldc	0x2
			iload	0
			imul
		invokevirtual	java/io/PrintStream/print(I)V
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		iload	0
		invokevirtual	java/io/PrintStream/print(I)V


EndGlobal:
return
.end method
.method public static one(II)I

.limit locals 2
.limit stack 5
.var 0 is a I from BeginGlobal to EndGlobal
.var 1 is b I from BeginGlobal to EndGlobal
BeginGlobal:
	.line 12
	invokestatic function4/SayHi()V

				iload	0
				ldc	0x1
				isub
			iload	1
			imul
		ldc	0x1
		iadd
	ireturn

EndGlobal:
.end method
.method public static two(IZ)I

.limit locals 3
.limit stack 5
.var 0 is c I from BeginGlobal to EndGlobal
.var 1 is b Z from BeginGlobal to EndGlobal
BeginGlobal:
.var 2 is d I from BeginGlobal to EndGlobal
	.line 19
	iload	0
	ldc	0x3
	invokestatic function4/one(II)I
	istore	0

	.line 20
	ldc	0x4
	ldc	0x5
	invokestatic function4/one(II)I
	istore	2

	.line 21
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		iload	0
		invokevirtual	java/io/PrintStream/print(I)V
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		iload	2
		invokevirtual	java/io/PrintStream/print(I)V
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		ldc	"\n"
		invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V


		iload	0
		iload	2
		if_icmplt	CmpLT1293297651
		ldc	0x0
		goto	CmpLTDone1293297651
	CmpLT1293297651:
		ldc	0x1
	CmpLTDone1293297651:
	istore	1

	.line 23
		iload	1
		ldc	0x1
		if_icmpeq	If1840569843
		goto	IfElse1840569843
	If1840569843:
	.line 24
			iload	0
			iload	2
			imul
		ireturn
	IfElse1840569843:
	.line 26
			iload	0
			iload	2
			idiv
		ireturn
	IfDone1840569843:

EndGlobal:
.end method
.method public static three(I)V

.limit locals 3
.limit stack 5
.var 0 is a I from BeginGlobal to EndGlobal
BeginGlobal:
.var 1 is b I from BeginGlobal to EndGlobal

.var 2 is c I from BeginGlobal to EndGlobal
		ldc	0x3
		iload	0
		imul
	istore	1

	.line 35
	iload	1
		iload	1
		ldc	0x3
		if_icmpeq	CmpEQ469216171
		ldc	0x0
		goto	CmpEQDone469216171
	CmpEQ469216171:
		ldc	0x1
	CmpEQDone469216171:
	invokestatic function4/two(IZ)I
	istore	2

	.line 36
	iload	2
		iload	1
		ldc	0x3
		if_icmpeq	CmpEQ775344959
		ldc	0x0
		goto	CmpEQDone775344959
	CmpEQ775344959:
		ldc	0x1
	CmpEQDone775344959:
	invokestatic function4/two(IZ)I
	istore	1

	.line 37
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		iload	1
		invokevirtual	java/io/PrintStream/print(I)V
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		iload	2
		invokevirtual	java/io/PrintStream/print(I)V
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		ldc	"\n"
		invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V


EndGlobal:
return
.end method
.method public static main([Ljava/lang/String;)V

.limit locals 2
.limit stack 5
BeginGlobal:
.var 0 is i I from BeginGlobal to EndGlobal
		ldc	0x0
		istore	0
	LoopBegin1691192906:
		.line 44
			iload	0
			ldc	0xA
			imul
		invokestatic function4/three(I)V
			iload	0
			ldc	0x1
			iadd
		istore	0
			iload	0
			ldc	0x4
			if_icmplt	CmpLT32163837
			ldc	0x0
			goto	CmpLTDone32163837
		CmpLT32163837:
			ldc	0x1
		CmpLTDone32163837:
		ldc	0x1
		if_icmpeq	LoopBegin1691192906
	End1691192906:

EndGlobal:
return
.end method

