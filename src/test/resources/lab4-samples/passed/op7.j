.class public op7
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
	.line 2
		getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		0x0
		ldc		0x1
		if_icmple	Cmp987804220
		ldc		0x0
		goto		CmpDone987804220
	Cmp987804220:
		ldc		0x1
	CmpDone987804220:
		invokevirtual		java/io/PrintStream/print(Z)V

	.line 3
		getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		0x0
		ldc		0x0
		if_icmple	Cmp261523817
		ldc		0x0
		goto		CmpDone261523817
	Cmp261523817:
		ldc		0x1
	CmpDone261523817:
		invokevirtual		java/io/PrintStream/print(Z)V

	.line 4
		getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		0x1
		ldc		0x0
		if_icmple	Cmp40782197
		ldc		0x0
		goto		CmpDone40782197
	Cmp40782197:
		ldc		0x1
	CmpDone40782197:
		invokevirtual		java/io/PrintStream/print(Z)V

EndGlobal:
return
.end method

