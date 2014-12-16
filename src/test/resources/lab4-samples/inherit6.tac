Barn.____AddCow:
	BeginFunc 8 ;
	PushParam cow ;
	_tmp0 = *(cow) ;
	_tmp1 = *(_tmp0 + 16) ;
	ACall _tmp1 ;
	PopParams 4 ;
	EndFunc ;
VTable Barn =
	Barn.____AddCow,
; 
main:
	BeginFunc 232 ;
	_tmp2 = 1 ;
	_tmp3 = 4 ;
	_tmp4 = 0 ;
	_tmp5 = _tmp2 < _tmp4 ;
	_tmp6 = _tmp2 == _tmp4 ;
	_tmp7 = _tmp5 || _tmp6 ;
	IfZ _tmp7 Goto _L0 ;
	_tmp8 = "Decaf runtime error: Array size is <= 0\n" ;
	PushParam _tmp8 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L0:
	_tmp9 = _tmp2 * _tmp3 ;
	_tmp10 = _tmp3 + _tmp9 ;
	PushParam _tmp10 ;
	_tmp11 = LCall _Alloc ;
	PopParams 4 ;
	*(_tmp11) = _tmp2 ;
	c = _tmp11 ;
	_tmp12 = 0 ;
	_tmp13 = 4 ;
	_tmp14 = _tmp13 + _tmp12 ;
	PushParam _tmp14 ;
	_tmp15 = LCall _Alloc ;
	PopParams 4 ;
	_tmp16 = Barn ;
	*(_tmp15) = _tmp16 ;
	b = _tmp15 ;
	_tmp17 = 4 ;
	_tmp18 = 4 ;
	_tmp19 = _tmp18 + _tmp17 ;
	PushParam _tmp19 ;
	_tmp20 = LCall _Alloc ;
	PopParams 4 ;
	_tmp21 = Cow ;
	*(_tmp20) = _tmp21 ;
	a = _tmp20 ;
	_tmp22 = 24 ;
	PushParam _tmp22 ;
	PushParam a ;
	_tmp23 = *(a) ;
	_tmp24 = *(_tmp23) ;
	ACall _tmp24 ;
	PopParams 8 ;
	PushParam a ;
	_tmp25 = *(a) ;
	_tmp26 = *(_tmp25 + 12) ;
	ACall _tmp26 ;
	PopParams 4 ;
	_tmp27 = 4 ;
	_tmp28 = 4 ;
	_tmp29 = _tmp28 + _tmp27 ;
	PushParam _tmp29 ;
	_tmp30 = LCall _Alloc ;
	PopParams 4 ;
	_tmp31 = Jersey ;
	*(_tmp30) = _tmp31 ;
	j = _tmp30 ;
	_tmp32 = 33 ;
	PushParam _tmp32 ;
	PushParam j ;
	_tmp33 = *(j) ;
	_tmp34 = *(_tmp33) ;
	ACall _tmp34 ;
	PopParams 8 ;
	a = j ;
	PushParam a ;
	_tmp35 = *(a) ;
	_tmp36 = *(_tmp35 + 12) ;
	ACall _tmp36 ;
	PopParams 4 ;
	PushParam j ;
	_tmp37 = *(j) ;
	_tmp38 = *(_tmp37 + 12) ;
	ACall _tmp38 ;
	PopParams 4 ;
	PushParam j ;
	_tmp39 = *(j) ;
	_tmp40 = *(_tmp39 + 20) ;
	ACall _tmp40 ;
	PopParams 4 ;
	PushParam a ;
	PushParam b ;
	_tmp41 = *(b) ;
	_tmp42 = *(_tmp41) ;
	ACall _tmp42 ;
	PopParams 8 ;
	PushParam j ;
	PushParam b ;
	_tmp43 = *(b) ;
	_tmp44 = *(_tmp43) ;
	ACall _tmp44 ;
	PopParams 8 ;
	EndFunc ;
Jersey.____Method3:
	BeginFunc 12 ;
	_tmp45 = "Jersey3" ;
	PushParam _tmp45 ;
	LCall _PrintString ;
	PopParams 4 ;
	PushParam this ;
	_tmp46 = *(this) ;
	_tmp47 = *(_tmp46 + 12) ;
	ACall _tmp47 ;
	PopParams 4 ;
	EndFunc ;
Jersey.____Method1:
	BeginFunc 12 ;
	_tmp48 = "Jersey" ;
	PushParam _tmp48 ;
	LCall _PrintString ;
	PopParams 4 ;
	PushParam this ;
	_tmp49 = *(this) ;
	_tmp50 = *(_tmp49 + 8) ;
	ACall _tmp50 ;
	PopParams 4 ;
	EndFunc ;
VTable Jersey =
	Animal.____Init,
	Jersey.____Method1,
	Animal.____PrintSelf,
	Cow.____Method2,
	Jersey.____Method1,
	Jersey.____Method3,
	Jersey.____Method1,
; 
Animal.____Init:
	BeginFunc 0 ;
	*(this + 4) = n ;
	EndFunc ;
Animal.____Method1:
	BeginFunc 12 ;
	_tmp51 = "Animal" ;
	PushParam _tmp51 ;
	LCall _PrintString ;
	PopParams 4 ;
	PushParam this ;
	_tmp52 = *(this) ;
	_tmp53 = *(_tmp52 + 8) ;
	ACall _tmp53 ;
	PopParams 4 ;
	EndFunc ;
Animal.____PrintSelf:
	BeginFunc 12 ;
	_tmp54 = "num1 = " ;
	PushParam _tmp54 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp55 = *(this + 4) ;
	PushParam _tmp55 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp56 = "\n" ;
	PushParam _tmp56 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
VTable Animal =
	Animal.____Init,
	Animal.____Method1,
	Animal.____PrintSelf,
; 
Cow.____Method2:
	BeginFunc 12 ;
	_tmp57 = "Cow2" ;
	PushParam _tmp57 ;
	LCall _PrintString ;
	PopParams 4 ;
	PushParam this ;
	_tmp58 = *(this) ;
	_tmp59 = *(_tmp58 + 16) ;
	ACall _tmp59 ;
	PopParams 4 ;
	EndFunc ;
Cow.____Method1:
	BeginFunc 12 ;
	_tmp60 = "Cow" ;
	PushParam _tmp60 ;
	LCall _PrintString ;
	PopParams 4 ;
	PushParam this ;
	_tmp61 = *(this) ;
	_tmp62 = *(_tmp61 + 8) ;
	ACall _tmp62 ;
	PopParams 4 ;
	EndFunc ;
VTable Cow =
	Animal.____Init,
	Cow.____Method1,
	Animal.____PrintSelf,
	Cow.____Method2,
	Cow.____Method1,
; 
