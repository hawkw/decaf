Animal.____Method1:
	BeginFunc 4 ;
	_tmp0 = "Animal" ;
	PushParam _tmp0 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
VTable Animal =
	Animal.____Method1,
; 
Cow.____Method1:
	BeginFunc 4 ;
	_tmp1 = "Cow" ;
	PushParam _tmp1 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
VTable Cow =
	Cow.____Method1,
	Cow.____Method1,
; 
Jersey.____Method1:
	BeginFunc 4 ;
	_tmp2 = "Jersey" ;
	PushParam _tmp2 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
VTable Jersey =
	Jersey.____Method1,
	Jersey.____Method1,
	Jersey.____Method1,
; 
main:
	BeginFunc 100 ;
	_tmp3 = 0 ;
	_tmp4 = 4 ;
	_tmp5 = _tmp4 + _tmp3 ;
	PushParam _tmp5 ;
	_tmp6 = LCall _Alloc ;
	PopParams 4 ;
	_tmp7 = Animal ;
	*(_tmp6) = _tmp7 ;
	a = _tmp6 ;
	PushParam a ;
	_tmp8 = *(a) ;
	_tmp9 = *(_tmp8) ;
	ACall _tmp9 ;
	PopParams 4 ;
	_tmp10 = 0 ;
	_tmp11 = 4 ;
	_tmp12 = _tmp11 + _tmp10 ;
	PushParam _tmp12 ;
	_tmp13 = LCall _Alloc ;
	PopParams 4 ;
	_tmp14 = Cow ;
	*(_tmp13) = _tmp14 ;
	a = _tmp13 ;
	PushParam a ;
	_tmp15 = *(a) ;
	_tmp16 = *(_tmp15) ;
	ACall _tmp16 ;
	PopParams 4 ;
	_tmp17 = 0 ;
	_tmp18 = 4 ;
	_tmp19 = _tmp18 + _tmp17 ;
	PushParam _tmp19 ;
	_tmp20 = LCall _Alloc ;
	PopParams 4 ;
	_tmp21 = Jersey ;
	*(_tmp20) = _tmp21 ;
	a = _tmp20 ;
	PushParam a ;
	_tmp22 = *(a) ;
	_tmp23 = *(_tmp22) ;
	ACall _tmp23 ;
	PopParams 4 ;
	EndFunc ;
