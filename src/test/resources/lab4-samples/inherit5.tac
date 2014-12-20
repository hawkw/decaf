Animal.____Method:
	BeginFunc 4 ;
	_tmp0 = "Hello from Animal\n" ;
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
Animal.____AMethod:
	BeginFunc 4 ;
	_tmp2 = "not overridden\n" ;
	PushParam _tmp2 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
VTable Animal =
	Animal.____Method,
	Animal.____Method1,
	Animal.____AMethod,
; 
Cow.____Method:
	BeginFunc 4 ;
	_tmp3 = "Hello from Cow" ;
	PushParam _tmp3 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
Cow.____Method2:
	BeginFunc 12 ;
	_tmp4 = "Cow2" ;
	PushParam _tmp4 ;
	LCall _PrintString ;
	PopParams 4 ;
	PushParam this ;
	_tmp5 = *(this) ;
	_tmp6 = *(_tmp5 + 20) ;
	ACall _tmp6 ;
	PopParams 4 ;
	EndFunc ;
Cow.____Method1:
	BeginFunc 4 ;
	_tmp7 = "Cow" ;
	PushParam _tmp7 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
Cow.____AMethod:
	BeginFunc 0 ;
	EndFunc ;
VTable Cow =
	Cow.____Method,
	Cow.____Method1,
	Cow.____AMethod,
	Cow.____Method,
	Cow.____Method2,
	Cow.____Method1,
	Cow.____AMethod,
; 
Jersey.____Method:
	BeginFunc 4 ;
	_tmp8 = "Hello from Jersey." ;
	PushParam _tmp8 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
Jersey.____Method3:
	BeginFunc 12 ;
	_tmp9 = "Jersey3" ;
	PushParam _tmp9 ;
	LCall _PrintString ;
	PopParams 4 ;
	PushParam this ;
	_tmp10 = *(this) ;
	_tmp11 = *(_tmp10 + 16) ;
	ACall _tmp11 ;
	PopParams 4 ;
	EndFunc ;
Jersey.____Method1:
	BeginFunc 4 ;
	_tmp12 = "Jersey" ;
	PushParam _tmp12 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
VTable Jersey =
	Jersey.____Method,
	Jersey.____Method1,
	Cow.____AMethod,
	Jersey.____Method,
	Cow.____Method2,
	Jersey.____Method1,
	Cow.____AMethod,
	Jersey.____Method,
	Jersey.____Method3,
	Jersey.____Method1,
; 
main:
	BeginFunc 100 ;
	_tmp13 = 0 ;
	_tmp14 = 4 ;
	_tmp15 = _tmp14 + _tmp13 ;
	PushParam _tmp15 ;
	_tmp16 = LCall _Alloc ;
	PopParams 4 ;
	_tmp17 = Cow ;
	*(_tmp16) = _tmp17 ;
	a = _tmp16 ;
	PushParam a ;
	_tmp18 = *(a) ;
	_tmp19 = *(_tmp18 + 16) ;
	ACall _tmp19 ;
	PopParams 4 ;
	_tmp20 = 0 ;
	_tmp21 = 4 ;
	_tmp22 = _tmp21 + _tmp20 ;
	PushParam _tmp22 ;
	_tmp23 = LCall _Alloc ;
	PopParams 4 ;
	_tmp24 = Jersey ;
	*(_tmp23) = _tmp24 ;
	j = _tmp23 ;
	a = j ;
	PushParam a ;
	_tmp25 = *(a) ;
	_tmp26 = *(_tmp25 + 16) ;
	ACall _tmp26 ;
	PopParams 4 ;
	PushParam j ;
	_tmp27 = *(j) ;
	_tmp28 = *(_tmp27 + 32) ;
	ACall _tmp28 ;
	PopParams 4 ;
	PushParam a ;
	_tmp29 = *(a) ;
	_tmp30 = *(_tmp29 + 12) ;
	ACall _tmp30 ;
	PopParams 4 ;
	EndFunc ;
