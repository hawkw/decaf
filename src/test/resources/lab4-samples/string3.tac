main:
	BeginFunc 56 ;
	_tmp0 = "not" ;
	s = _tmp0 ;
	_tmp2 = "something else" ;
	PushParam _tmp2 ;
	PushParam s ;
	_tmp3 = LCall _StringEqual ;
	PopParams 8 ;
	IfZ _tmp3 Goto _L1 ;
	_tmp4 = 0 ;
	_tmp1 = _tmp4 ;
	Goto _L0 ;
_L1:
	_tmp5 = 1 ;
	_tmp1 = _tmp5 ;
_L0:
	PushParam _tmp1 ;
	LCall _PrintBool ;
	PopParams 4 ;
	_tmp7 = "not" ;
	PushParam _tmp7 ;
	PushParam s ;
	_tmp8 = LCall _StringEqual ;
	PopParams 8 ;
	IfZ _tmp8 Goto _L3 ;
	_tmp9 = 0 ;
	_tmp6 = _tmp9 ;
	Goto _L2 ;
_L3:
	_tmp10 = 1 ;
	_tmp6 = _tmp10 ;
_L2:
	PushParam _tmp6 ;
	LCall _PrintBool ;
	PopParams 4 ;
	EndFunc ;
