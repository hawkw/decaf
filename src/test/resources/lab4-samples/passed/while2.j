.class public while2
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
	LoopBegin311325004:
		.line 4
			getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		0x1
			invokevirtual		java/io/PrintStream/print(I)V

		.line 5
		goto		End311325004

		.line 6
			getstatic		java/lang/System/out Ljava/io/PrintStream;
		ldc		0x2
			invokevirtual		java/io/PrintStream/print(I)V
		ldc		0x1
		ldc		0x1
		if_icmpeq		LoopBegin311325004
	End311325004:

EndGlobal:
return
.end method

