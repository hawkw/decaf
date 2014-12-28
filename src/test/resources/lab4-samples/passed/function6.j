.source function6.decaf
.class public function6
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

.method public static sum(I)I

.limit locals 1
.limit stack 5
.var 0 is s I from BeginGlobal to EndGlobal
BeginGlobal:
	.line 2
			iload	0
			ldc	0x0
			if_icmpeq	CmpEQ1251673027
			ldc	0x0
			goto	CmpEQDone1251673027
		CmpEQ1251673027:
			ldc	0x1
		CmpEQDone1251673027:
		ldc	0x1
		if_icmpne	IfNot2047568224
	.line 2
		iload	0
		ireturn
	IfNot2047568224:

		iload	0
		.line 3
			iload	0
			ldc	0x1
			isub
		invokestatic function6/sum(I)I
		iadd
	ireturn

EndGlobal:
.end method
.method public static main([Ljava/lang/String;)V

.limit locals 1
.limit stack 5
BeginGlobal:
	.line 8
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		.line 8
		ldc	0xEA
		invokestatic function6/sum(I)I
		invokevirtual	java/io/PrintStream/print(I)V


EndGlobal:
return
.end method

