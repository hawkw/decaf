.source io3.decaf
.class public io3
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

.limit locals 1
.limit stack 5
BeginGlobal:
	.line 3
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		ldc	0x4
		invokevirtual	java/io/PrintStream/print(I)V
		getstatic	java/lang/System/out Ljava/io/PrintStream;
				ldc	0x4
				ldc	0x8
				imul
			ldc	0x1
			iadd
		invokevirtual	java/io/PrintStream/print(I)V
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		ldc	"hello"
		invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		ldc	0x1
		invokevirtual	java/io/PrintStream/print(Z)V
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		ldc	"cs143\n"
		invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V
		getstatic	java/lang/System/out Ljava/io/PrintStream;
				ldc	0x8
				ldc	0x3
				isub
			ldc	0x2
			isub
		invokevirtual	java/io/PrintStream/print(I)V


EndGlobal:
return
.end method

