main:
	BeginFunc 96 ;
	_tmp1 = 0 ;
	_tmp2 = 1 ;
	_tmp3 = _tmp1 == _tmp2 ;
	IfZ _tmp3 Goto _L1 ;
	_tmp4 = 0 ;
	_tmp0 = _tmp4 ;
	Goto _L0 ;
_L1:
	_tmp5 = 1 ;
	_tmp0 = _tmp5 ;
_L0:
	PushParam _tmp0 ;
	LCall _PrintBool ;
	PopParams 4 ;
	_tmp7 = 0 ;
	_tmp8 = 0 ;
	_tmp9 = _tmp7 == _tmp8 ;
	IfZ _tmp9 Goto _L3 ;
	_tmp10 = 0 ;
	_tmp6 = _tmp10 ;
	Goto _L2 ;
_L3:
	_tmp11 = 1 ;
	_tmp6 = _tmp11 ;
_L2:
	PushParam _tmp6 ;
	LCall _PrintBool ;
	PopParams 4 ;
	_tmp13 = 1 ;
	_tmp14 = 1 ;
	_tmp15 = _tmp13 == _tmp14 ;
	IfZ _tmp15 Goto _L5 ;
	_tmp16 = 0 ;
	_tmp12 = _tmp16 ;
	Goto _L4 ;
_L5:
	_tmp17 = 1 ;
	_tmp12 = _tmp17 ;
_L4:
	PushParam _tmp12 ;
	LCall _PrintBool ;
	PopParams 4 ;
	_tmp19 = 1 ;
	_tmp20 = 0 ;
	_tmp21 = _tmp19 == _tmp20 ;
	IfZ _tmp21 Goto _L7 ;
	_tmp22 = 0 ;
	_tmp18 = _tmp22 ;
	Goto _L6 ;
_L7:
	_tmp23 = 1 ;
	_tmp18 = _tmp23 ;
_L6:
	PushParam _tmp18 ;
	LCall _PrintBool ;
	PopParams 4 ;
	EndFunc ;
