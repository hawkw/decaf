main:
	BeginFunc 100 ;
	_tmp0 = 2 ;
	i = _tmp0 ;
_L0:
	_tmp1 = 7 ;
	_tmp2 = i < _tmp1 ;
	IfZ _tmp2 Goto _L1 ;
	_tmp3 = 2 ;
	j = _tmp3 ;
_L2:
	_tmp4 = 6 ;
	_tmp5 = j < _tmp4 ;
	IfZ _tmp5 Goto _L3 ;
	_tmp6 = i * j ;
	_tmp7 = 10 ;
	_tmp8 = _tmp6 == _tmp7 ;
	IfZ _tmp8 Goto _L4 ;
	Goto _L3 ;
	Goto _L5 ;
_L4:
_L5:
	PushParam i ;
	LCall _PrintInt ;
	PopParams 4 ;
	PushParam j ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp9 = "\n" ;
	PushParam _tmp9 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp10 = 1 ;
	_tmp11 = j + _tmp10 ;
	j = _tmp11 ;
	Goto _L2 ;
_L3:
	_tmp12 = 1 ;
	_tmp13 = i + _tmp12 ;
	i = _tmp13 ;
	Goto _L0 ;
_L1:
	_tmp14 = "done" ;
	PushParam _tmp14 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
