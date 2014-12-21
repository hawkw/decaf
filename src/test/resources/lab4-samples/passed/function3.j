.source function3.decaf
.class public function3
.super java/lang/Object
;
; standard initializer (calls java.lang.Object's initializer)
;
.method public <init>()V
aload_0
invokenonvirtual java/lang/Object/<init>()V
return
.end method

.method public static f()V

.limit locals 0
.limit stack 5
BeginGlobal:
	.line 2
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		ldc	0x1
		invokevirtual	java/io/PrintStream/print(I)V


	.line 3
	return

	.line 4
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		ldc	0x2
		invokevirtual	java/io/PrintStream/print(I)V


EndGlobal:
.end method
.method public static g()I

.limit locals 0
.limit stack 5
BeginGlobal:
	LoopBegin141675550:
		.line 9
			getstatic	java/lang/System/out Ljava/io/PrintStream;
			ldc	0x3
			invokevirtual	java/io/PrintStream/print(I)V


		ldc	0x5
		ireturn

		.line 11
			getstatic	java/lang/System/out Ljava/io/PrintStream;
			ldc	0x4
			invokevirtual	java/io/PrintStream/print(I)V

		ldc	0x1
		ldc	0x1
		if_icmpeq	LoopBegin141675550
	End141675550:

EndGlobal:
.end method
.method public static main([Ljava/lang/String;)V

.limit locals 1
.limit stack 5
BeginGlobal:
	.line 15
	invokestatic function3/f()V

	.line 16
	invokestatic function3/g()I

	.line 17
	return

	.line 18
		getstatic	java/lang/System/out Ljava/io/PrintStream;
		ldc	0x5
		invokevirtual	java/io/PrintStream/print(I)V


EndGlobal:
return
.end method

