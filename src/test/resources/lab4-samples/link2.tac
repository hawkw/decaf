Foo.main:
	BeginFunc 4 ;
	_tmp0 = "Foo main" ;
	PushParam _tmp0 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
VTable Foo =
	Foo.main,
; 
main:
	BeginFunc 36 ;
	_tmp1 = 0 ;
	_tmp2 = 4 ;
	_tmp3 = _tmp2 + _tmp1 ;
	PushParam _tmp3 ;
	_tmp4 = LCall _Alloc ;
	PopParams 4 ;
	_tmp5 = Foo ;
	*(_tmp4) = _tmp5 ;
	f = _tmp4 ;
	PushParam f ;
	_tmp6 = *(f) ;
	_tmp7 = *(_tmp6) ;
	ACall _tmp7 ;
	PopParams 4 ;
	EndFunc ;
