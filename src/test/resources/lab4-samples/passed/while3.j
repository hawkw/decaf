.class public while3
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
	LoopBegin836614847:
		.line 2
		goto		End836614847
		ldc		0x1
		ldc		0x1
		if_icmpeq		LoopBegin836614847
	End836614847:

	.line 3
		getstatic		java/lang/System/out Ljava/io/PrintStream;
	ldc		0x1
		invokevirtual		java/io/PrintStream/print(I)V

EndGlobal:
return
.end method

