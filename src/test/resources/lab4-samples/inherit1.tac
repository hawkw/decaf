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
main:
	BeginFunc 48 ;
	_tmp16 = 16 ;
	_tmp17 = 4 ;
	_tmp18 = _tmp17 + _tmp16 ;
	PushParam _tmp18 ;
	_tmp19 = LCall _Alloc ;
	PopParams 4 ;
	_tmp20 = Cow ;
	*(_tmp19) = _tmp20 ;
	b = _tmp19 ;
	PushParam b ;
	_tmp21 = *(b) ;
	_tmp22 = *(_tmp21 + 4) ;
	ACall _tmp22 ;
	PopParams 4 ;
	PushParam b ;
	_tmp23 = *(b) ;
	_tmp24 = *(_tmp23 + 8) ;
	ACall _tmp24 ;
	PopParams 4 ;
	EndFunc ;
