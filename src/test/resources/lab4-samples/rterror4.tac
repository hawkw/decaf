main:
	BeginFunc 56 ;
	_tmp0 = 5 ;
	_tmp1 = 0 ;
	_tmp2 = _tmp1 - _tmp0 ;
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
	arr = _tmp11 ;
	_tmp12 = "shouldn't get here" ;
	PushParam _tmp12 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
