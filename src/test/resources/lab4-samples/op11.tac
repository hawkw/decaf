main:
	BeginFunc 32 ;
	_tmp1 = 1 ;
	IfZ _tmp1 Goto _L1 ;
	_tmp2 = 0 ;
	_tmp0 = _tmp2 ;
	Goto _L0 ;
_L1:
	_tmp3 = 1 ;
	_tmp0 = _tmp3 ;
_L0:
	PushParam _tmp0 ;
	LCall _PrintBool ;
	PopParams 4 ;
	_tmp5 = 0 ;
	IfZ _tmp5 Goto _L3 ;
	_tmp6 = 0 ;
	_tmp4 = _tmp6 ;
	Goto _L2 ;
_L3:
	_tmp7 = 1 ;
	_tmp4 = _tmp7 ;
_L2:
	PushParam _tmp4 ;
	LCall _PrintBool ;
	PopParams 4 ;
	EndFunc ;
