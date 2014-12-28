.source function1.decaf
.class public function1
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

.method public static f(I)V

.limit locals 1
.limit stack 5
.var 0 is fe I from BeginGlobal to EndGlobal
BeginGlobal:
	.line 2
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		iload	0
		invokevirtual	java/io/PrintStream/print(I)V


EndGlobal:
return
.end method
.method public static ff()V

.limit locals 0
.limit stack 5
BeginGlobal:
	.line 6
	ldc	0x22
	invokestatic function1/f(I)V

EndGlobal:
return
.end method
.method public static g(ILjava/lang/String;Z)V

.limit locals 3
.limit stack 5
.var 0 is a I from BeginGlobal to EndGlobal
.var 1 is b Ljava/lang/String; from BeginGlobal to EndGlobal
.var 2 is c Z from BeginGlobal to EndGlobal
BeginGlobal:
	.line 10
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		iload	2
		invokevirtual	java/io/PrintStream/print(Z)V
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		aload	1
		invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		iload	0
		invokevirtual	java/io/PrintStream/print(I)V


EndGlobal:
return
.end method
.method public static main([Ljava/lang/String;)V

.limit locals 1
.limit stack 5
BeginGlobal:
	.line 14
	ldc	0xC
	invokestatic function1/f(I)V

	.line 15
	invokestatic function1/ff()V

	.line 16
	ldc	0x2
	ldc	" and "
	ldc	0x1
	invokestatic function1/g(ILjava/lang/String;Z)V

	.line 17
			ldc	0xA
				ldc	0x3
				ldc	0x2
				irem
			iadd
		ldc	0x5
		iadd
	invokestatic function1/f(I)V

EndGlobal:
return
.end method

