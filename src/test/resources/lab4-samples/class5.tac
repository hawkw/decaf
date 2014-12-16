Cow.____Init:
	BeginFunc 8 ;
	_tmp0 = "Moo" ;
	PushParam _tmp0 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp1 = 0 ;
	Return _tmp1 ;
	EndFunc ;
VTable Cow =
	Cow.____Init,
; 
Barn.____Init:
	BeginFunc 36 ;
	_tmp2 = 0 ;
	_tmp3 = 4 ;
	_tmp4 = _tmp3 + _tmp2 ;
	PushParam _tmp4 ;
	_tmp5 = LCall _Alloc ;
	PopParams 4 ;
	_tmp6 = Cow ;
	*(_tmp5) = _tmp6 ;
	PushParam _tmp5 ;
	_tmp7 = *(_tmp5) ;
	_tmp8 = *(_tmp7) ;
	_tmp9 = ACall _tmp8 ;
	PopParams 4 ;
	b = _tmp9 ;
	EndFunc ;
VTable Barn =
	Barn.____Init,
; 
main:
	BeginFunc 28 ;
	_tmp10 = 0 ;
	_tmp11 = 4 ;
	_tmp12 = _tmp11 + _tmp10 ;
	PushParam _tmp12 ;
	_tmp13 = LCall _Alloc ;
	PopParams 4 ;
	_tmp14 = Barn ;
	*(_tmp13) = _tmp14 ;
	PushParam _tmp13 ;
	_tmp15 = *(_tmp13) ;
	_tmp16 = *(_tmp15) ;
	ACall _tmp16 ;
	PopParams 4 ;
	EndFunc ;
