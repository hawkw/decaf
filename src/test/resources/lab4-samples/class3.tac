Binky.____Method1:
	BeginFunc 4 ;
	_tmp0 = 1 ;
	PushParam _tmp0 ;
	LCall _PrintInt ;
	PopParams 4 ;
	EndFunc ;
Binky.____Method2:
	BeginFunc 20 ;
	PushParam this ;
	_tmp1 = *(this) ;
	_tmp2 = *(_tmp1) ;
	ACall _tmp2 ;
	PopParams 4 ;
	_tmp3 = 2 ;
	PushParam _tmp3 ;
	LCall _PrintInt ;
	PopParams 4 ;
	PushParam this ;
	_tmp4 = *(this) ;
	_tmp5 = *(_tmp4) ;
	ACall _tmp5 ;
	PopParams 4 ;
	EndFunc ;
Binky.____Method3:
	BeginFunc 20 ;
	PushParam b ;
	_tmp6 = *(b) ;
	_tmp7 = *(_tmp6) ;
	ACall _tmp7 ;
	PopParams 4 ;
	_tmp8 = 2 ;
	PushParam _tmp8 ;
	LCall _PrintInt ;
	PopParams 4 ;
	PushParam this ;
	_tmp9 = *(this) ;
	_tmp10 = *(_tmp9 + 4) ;
	ACall _tmp10 ;
	PopParams 4 ;
	EndFunc ;
VTable Binky =
	Binky.____Method1,
	Binky.____Method2,
	Binky.____Method3,
; 
main:
	BeginFunc 64 ;
	_tmp11 = 0 ;
	_tmp12 = 4 ;
	_tmp13 = _tmp12 + _tmp11 ;
	PushParam _tmp13 ;
	_tmp14 = LCall _Alloc ;
	PopParams 4 ;
	_tmp15 = Binky ;
	*(_tmp14) = _tmp15 ;
	b = _tmp14 ;
	_tmp16 = 0 ;
	_tmp17 = 4 ;
	_tmp18 = _tmp17 + _tmp16 ;
	PushParam _tmp18 ;
	_tmp19 = LCall _Alloc ;
	PopParams 4 ;
	_tmp20 = Binky ;
	*(_tmp19) = _tmp20 ;
	c = _tmp19 ;
	PushParam c ;
	PushParam b ;
	_tmp21 = *(b) ;
	_tmp22 = *(_tmp21 + 8) ;
	ACall _tmp22 ;
	PopParams 8 ;
	EndFunc ;
