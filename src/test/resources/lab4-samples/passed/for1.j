.source for1.decaf
.class public for1
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

.method public static main([Ljava/lang/String;)V

.limit locals 2
.limit stack 5
BeginGlobal:
.var 0 is i I from BeginGlobal to EndGlobal
	ldc	0x0
	istore	0

	LoopBegin767889098:
		.line 5
			getstatic	java/lang/System/out Ljava/io/PrintStream;
			iload	0
			invokevirtual	java/io/PrintStream/print(I)V


			iload	0
			ldc	0x1
			iadd
		istore	0
			iload	0
			ldc	0xA
			if_icmplt	CmpLT573218879
			ldc	0x0
			goto	CmpLTDone573218879
		CmpLT573218879:
			ldc	0x1
		CmpLTDone573218879:
		ldc	0x1
		if_icmpeq	LoopBegin767889098
	End767889098:

	.line 8
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		ldc	"done\n"
		invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V


EndGlobal:
return
.end method

