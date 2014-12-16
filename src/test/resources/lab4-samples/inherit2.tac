Animal.____Method1:
	BeginFunc 8 ;
	_tmp0 = *(this + 4) ;
	PushParam _tmp0 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp1 = *(this + 8) ;
	PushParam _tmp1 ;
	LCall _PrintInt ;
	PopParams 4 ;
	EndFunc ;
VTable Animal =
	Animal.____Method1,
; 
Cow.____Init:
	BeginFunc 32 ;
	_tmp2 = 10 ;
	*(this + 4) = _tmp2 ;
	_tmp3 = *(this + 4) ;
	_tmp4 = 2 ;
	_tmp5 = _tmp3 * _tmp4 ;
	*(this + 8) = _tmp5 ;
	_tmp6 = 28 ;
	*(this + 12) = _tmp6 ;
	_tmp7 = 9 ;
	_tmp8 = 0 ;
	_tmp9 = _tmp8 - _tmp7 ;
	*(this + 16) = _tmp9 ;
	EndFunc ;
Cow.____Method2:
	BeginFunc 24 ;
	_tmp10 = *(this + 4) ;
	PushParam _tmp10 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp11 = *(this + 8) ;
	PushParam _tmp11 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp12 = *(this + 12) ;
	PushParam _tmp12 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp13 = *(this + 16) ;
	PushParam _tmp13 ;
	LCall _PrintInt ;
	PopParams 4 ;
	PushParam this ;
	_tmp14 = *(this) ;
	_tmp15 = *(_tmp14) ;
	ACall _tmp15 ;
	PopParams 4 ;
	EndFunc ;
VTable Cow =
	Animal.____Method1,
	Cow.____Init,
	Cow.____Method2,
; 
Jersey.____Method3:
	BeginFunc 24 ;
	PushParam this ;
	_tmp16 = *(this) ;
	_tmp17 = *(_tmp16 + 4) ;
	ACall _tmp17 ;
	PopParams 4 ;
	_tmp18 = 25 ;
	*(this + 20) = _tmp18 ;
	PushParam this ;
	_tmp19 = *(this) ;
	_tmp20 = *(_tmp19 + 8) ;
	ACall _tmp20 ;
	PopParams 4 ;
	_tmp21 = *(this + 20) ;
	PushParam _tmp21 ;
	LCall _PrintInt ;
	PopParams 4 ;
	EndFunc ;
VTable Jersey =
	Animal.____Method1,
	Cow.____Init,
	Cow.____Method2,
	Jersey.____Method3,
; 
main:
	BeginFunc 48 ;
	_tmp22 = 20 ;
	_tmp23 = 4 ;
	_tmp24 = _tmp23 + _tmp22 ;
	PushParam _tmp24 ;
	_tmp25 = LCall _Alloc ;
	PopParams 4 ;
	_tmp26 = Jersey ;
	*(_tmp25) = _tmp26 ;
	b = _tmp25 ;
	PushParam b ;
	_tmp27 = *(b) ;
	_tmp28 = *(_tmp27 + 4) ;
	ACall _tmp28 ;
	PopParams 4 ;
	PushParam b ;
	_tmp29 = *(b) ;
	_tmp30 = *(_tmp29 + 12) ;
	ACall _tmp30 ;
	PopParams 4 ;
	EndFunc ;
