main:
	BeginFunc 68 ;
	_tmp0 = 0 ;
	i = _tmp0 ;
	_tmp1 = 12 ;
	_tmp2 = _tmp1 < i ;
	_tmp3 = 12 ;
	_tmp4 = i < _tmp3 ;
	_tmp5 = _tmp2 || _tmp4 ;
	_tmp6 = 3 ;
	_tmp7 = 5 ;
	_tmp8 = _tmp6 == _tmp7 ;
	_tmp9 = 1 ;
	_tmp10 = _tmp8 && _tmp9 ;
	_tmp11 = _tmp5 || _tmp10 ;
	IfZ _tmp11 Goto _L0 ;
	_tmp12 = 1 ;
	PushParam _tmp12 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Goto _L1 ;
_L0:
_L1:
	_tmp13 = 2 ;
	PushParam _tmp13 ;
	LCall _PrintInt ;
	PopParams 4 ;
	EndFunc ;
