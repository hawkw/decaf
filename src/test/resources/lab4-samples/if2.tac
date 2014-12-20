main:
	BeginFunc 108 ;
	_tmp0 = 1 ;
	_tmp1 = 1 ;
	_tmp2 = _tmp0 == _tmp1 ;
	IfZ _tmp2 Goto _L0 ;
	_tmp3 = 1 ;
	PushParam _tmp3 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Goto _L1 ;
_L0:
	_tmp4 = 2 ;
	_tmp5 = 1 ;
	_tmp6 = _tmp4 == _tmp5 ;
	IfZ _tmp6 Goto _L2 ;
	_tmp7 = 2 ;
	PushParam _tmp7 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Goto _L3 ;
_L2:
	_tmp8 = 3 ;
	PushParam _tmp8 ;
	LCall _PrintInt ;
	PopParams 4 ;
_L3:
_L1:
	_tmp9 = 1 ;
	_tmp10 = 2 ;
	_tmp11 = _tmp9 == _tmp10 ;
	IfZ _tmp11 Goto _L4 ;
	_tmp12 = 1 ;
	PushParam _tmp12 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Goto _L5 ;
_L4:
	_tmp13 = 2 ;
	_tmp14 = 2 ;
	_tmp15 = _tmp13 == _tmp14 ;
	IfZ _tmp15 Goto _L6 ;
	_tmp16 = 2 ;
	PushParam _tmp16 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Goto _L7 ;
_L6:
	_tmp17 = 3 ;
	PushParam _tmp17 ;
	LCall _PrintInt ;
	PopParams 4 ;
_L7:
_L5:
	_tmp18 = 1 ;
	_tmp19 = 2 ;
	_tmp20 = _tmp18 == _tmp19 ;
	IfZ _tmp20 Goto _L8 ;
	_tmp21 = 1 ;
	PushParam _tmp21 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Goto _L9 ;
_L8:
	_tmp22 = 2 ;
	_tmp23 = 1 ;
	_tmp24 = _tmp22 == _tmp23 ;
	IfZ _tmp24 Goto _L10 ;
	_tmp25 = 2 ;
	PushParam _tmp25 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Goto _L11 ;
_L10:
	_tmp26 = 3 ;
	PushParam _tmp26 ;
	LCall _PrintInt ;
	PopParams 4 ;
_L11:
_L9:
	EndFunc ;
