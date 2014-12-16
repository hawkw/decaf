Animal.____MethodA:
	BeginFunc 4 ;
	_tmp0 = "Not overridden\n" ;
	PushParam _tmp0 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
Animal.____Method1:
	BeginFunc 4 ;
	_tmp1 = "Animal" ;
	PushParam _tmp1 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
VTable Animal =
	Animal.____MethodA,
	Animal.____Method1,
; 
Cow.____Method1:
	BeginFunc 4 ;
	_tmp2 = "Cow" ;
	PushParam _tmp2 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
VTable Cow =
	Animal.____MethodA,
	Cow.____Method1,
	Cow.____Method1,
; 
Jersey.____Method1:
	BeginFunc 4 ;
	_tmp3 = "Jersey" ;
	PushParam _tmp3 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
VTable Jersey =
	Animal.____MethodA,
	Jersey.____Method1,
	Jersey.____Method1,
	Jersey.____Method1,
; 
main:
	BeginFunc 144 ;
	_tmp4 = 0 ;
	_tmp5 = 4 ;
	_tmp6 = _tmp5 + _tmp4 ;
	PushParam _tmp6 ;
	_tmp7 = LCall _Alloc ;
	PopParams 4 ;
	_tmp8 = Animal ;
	*(_tmp7) = _tmp8 ;
	a = _tmp7 ;
	PushParam a ;
	_tmp9 = *(a) ;
	_tmp10 = *(_tmp9) ;
	ACall _tmp10 ;
	PopParams 4 ;
	PushParam a ;
	_tmp11 = *(a) ;
	_tmp12 = *(_tmp11 + 4) ;
	ACall _tmp12 ;
	PopParams 4 ;
	_tmp13 = 0 ;
	_tmp14 = 4 ;
	_tmp15 = _tmp14 + _tmp13 ;
	PushParam _tmp15 ;
	_tmp16 = LCall _Alloc ;
	PopParams 4 ;
	_tmp17 = Cow ;
	*(_tmp16) = _tmp17 ;
	c = _tmp16 ;
	PushParam c ;
	_tmp18 = *(c) ;
	_tmp19 = *(_tmp18) ;
	ACall _tmp19 ;
	PopParams 4 ;
	PushParam c ;
	_tmp20 = *(c) ;
	_tmp21 = *(_tmp20 + 8) ;
	ACall _tmp21 ;
	PopParams 4 ;
	_tmp22 = 0 ;
	_tmp23 = 4 ;
	_tmp24 = _tmp23 + _tmp22 ;
	PushParam _tmp24 ;
	_tmp25 = LCall _Alloc ;
	PopParams 4 ;
	_tmp26 = Jersey ;
	*(_tmp25) = _tmp26 ;
	j = _tmp25 ;
	PushParam j ;
	_tmp27 = *(j) ;
	_tmp28 = *(_tmp27) ;
	ACall _tmp28 ;
	PopParams 4 ;
	PushParam j ;
	_tmp29 = *(j) ;
	_tmp30 = *(_tmp29 + 12) ;
	ACall _tmp30 ;
	PopParams 4 ;
	EndFunc ;
