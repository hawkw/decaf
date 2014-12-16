____SayHi:
	BeginFunc 48 ;
	_tmp0 = 15 ;
	c = _tmp0 ;
	_tmp1 = 0 ;
	IfZ _tmp1 Goto _L0 ;
	Return  ;
	Goto _L1 ;
_L0:
_L1:
	_tmp2 = c * c ;
	_tmp3 = c - _tmp2 ;
	c = _tmp3 ;
	_tmp4 = 2 ;
	_tmp5 = _tmp4 * c ;
	PushParam _tmp5 ;
	LCall _PrintInt ;
	PopParams 4 ;
	PushParam c ;
	LCall _PrintInt ;
	PopParams 4 ;
	EndFunc ;
____one:
	BeginFunc 20 ;
	LCall ____SayHi ;
	_tmp6 = 1 ;
	_tmp7 = a - _tmp6 ;
	_tmp8 = _tmp7 * b ;
	_tmp9 = 1 ;
	_tmp10 = _tmp8 + _tmp9 ;
	Return _tmp10 ;
	EndFunc ;
____two:
	BeginFunc 56 ;
	_tmp11 = 3 ;
	PushParam _tmp11 ;
	PushParam c ;
	_tmp12 = LCall ____one ;
	PopParams 8 ;
	c = _tmp12 ;
	_tmp13 = 4 ;
	_tmp14 = 5 ;
	PushParam _tmp14 ;
	PushParam _tmp13 ;
	_tmp15 = LCall ____one ;
	PopParams 8 ;
	d = _tmp15 ;
	PushParam c ;
	LCall _PrintInt ;
	PopParams 4 ;
	PushParam d ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp16 = "\n" ;
	PushParam _tmp16 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp17 = c < d ;
	b = _tmp17 ;
	IfZ b Goto _L2 ;
	_tmp18 = c * d ;
	Return _tmp18 ;
	Goto _L3 ;
_L2:
	_tmp19 = c / d ;
	Return _tmp19 ;
_L3:
	EndFunc ;
____three:
	BeginFunc 68 ;
	_tmp20 = 3 ;
	_tmp21 = _tmp20 * a ;
	b = _tmp21 ;
	_tmp22 = 3 ;
	_tmp23 = b == _tmp22 ;
	PushParam _tmp23 ;
	PushParam b ;
	_tmp24 = LCall ____two ;
	PopParams 8 ;
	c = _tmp24 ;
	_tmp25 = 3 ;
	_tmp26 = b == _tmp25 ;
	PushParam _tmp26 ;
	PushParam c ;
	_tmp27 = LCall ____two ;
	PopParams 8 ;
	b = _tmp27 ;
	PushParam b ;
	LCall _PrintInt ;
	PopParams 4 ;
	PushParam c ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp28 = "\n" ;
	PushParam _tmp28 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
main:
	BeginFunc 48 ;
	_tmp29 = 0 ;
	i = _tmp29 ;
_L4:
	_tmp30 = 4 ;
	_tmp31 = i < _tmp30 ;
	IfZ _tmp31 Goto _L5 ;
	_tmp32 = 10 ;
	_tmp33 = i * _tmp32 ;
	PushParam _tmp33 ;
	_tmp34 = LCall ____three ;
	PopParams 4 ;
	_tmp35 = 1 ;
	_tmp36 = i + _tmp35 ;
	i = _tmp36 ;
	Goto _L4 ;
_L5:
	EndFunc ;
