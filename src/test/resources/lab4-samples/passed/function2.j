.source function2.decaf
.class public function2
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

.method public static f()I

.limit locals 0
.limit stack 5
BeginGlobal:
			ldc	0x5
			ldc	0xA
			imul
		ldc	0x4
		iadd
	ireturn

EndGlobal:
.end method
.method public static g(I)Ljava/lang/String;

.limit locals 1
.limit stack 5
.var 0 is a I from BeginGlobal to EndGlobal
BeginGlobal:
	.line 6
			iload	0
			ldc	0x5
			if_icmpeq	CmpEQ80792139
			ldc	0x0
			goto	CmpEQDone80792139
		CmpEQ80792139:
			ldc	0x1
		CmpEQDone80792139:
		ldc	0x1
		if_icmpeq	If111812458
		goto	IfElse111812458
	If111812458:
	.line 7
		ldc	"hello"
		areturn
	IfElse111812458:
	.line 9
		ldc	"world"
		areturn
	IfDone111812458:

EndGlobal:
.end method
.method public static main([Ljava/lang/String;)V

.limit locals 2
.limit stack 5
BeginGlobal:
.var 0 is s Ljava/lang/String; from BeginGlobal to EndGlobal
	.line 14
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		.line 14
		invokestatic function2/f()I
		invokevirtual	java/io/PrintStream/print(I)V


	.line 15
			ldc	0x4
			ldc	0x5
			imul
		ldc	0x2
		idiv
	invokestatic function2/g(I)Ljava/lang/String;
	astore	0

	.line 16
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		aload	0
		invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V


EndGlobal:
return
.end method

