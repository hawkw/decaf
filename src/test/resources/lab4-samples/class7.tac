Cow.____Moo:
	BeginFunc 20 ;
	_tmp0 = i + j ;
	_tmp1 = _tmp0 + k ;
	_tmp2 = _tmp1 + l ;
	_tmp3 = _tmp2 + m ;
	_tmp4 = _tmp3 + n ;
	Return _tmp4 ;
	EndFunc ;
Cow.____Method:
	BeginFunc 24 ;
	_tmp5 = 0 ;
	IfZ _tmp5 Goto _L0 ;
	PushParam a ;
	PushParam a ;
	PushParam a ;
	PushParam a ;
	PushParam a ;
	PushParam a ;
	PushParam this ;
	_tmp6 = *(this) ;
	_tmp7 = *(_tmp6) ;
	_tmp8 = ACall _tmp7 ;
	PopParams 28 ;
	Goto _L1 ;
_L0:
_L1:
	_tmp9 = 3 ;
	PushParam _tmp9 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp10 = 4 ;
	PushParam _tmp10 ;
	LCall _PrintInt ;
	PopParams 4 ;
	EndFunc ;
VTable Cow =
	Cow.____Moo,
	Cow.____Method,
; 
main:
	BeginFunc 40 ;
	_tmp11 = 0 ;
	_tmp12 = 4 ;
	_tmp13 = _tmp12 + _tmp11 ;
	PushParam _tmp13 ;
	_tmp14 = LCall _Alloc ;
	PopParams 4 ;
	_tmp15 = Cow ;
	*(_tmp14) = _tmp15 ;
	c = _tmp14 ;
	_tmp16 = 6 ;
	PushParam _tmp16 ;
	PushParam c ;
	_tmp17 = *(c) ;
	_tmp18 = *(_tmp17 + 4) ;
	ACall _tmp18 ;
	PopParams 8 ;
	EndFunc ;
