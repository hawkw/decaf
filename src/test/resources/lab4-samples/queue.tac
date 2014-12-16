QueueItem.____Init:
	BeginFunc 0 ;
	*(this + 4) = data ;
	*(this + 8) = next ;
	*(next + 12) = this ;
	*(this + 12) = prev ;
	*(prev + 8) = this ;
	EndFunc ;
QueueItem.____GetData:
	BeginFunc 4 ;
	_tmp0 = *(this + 4) ;
	Return _tmp0 ;
	EndFunc ;
QueueItem.____GetNext:
	BeginFunc 4 ;
	_tmp1 = *(this + 8) ;
	Return _tmp1 ;
	EndFunc ;
QueueItem.____GetPrev:
	BeginFunc 4 ;
	_tmp2 = *(this + 12) ;
	Return _tmp2 ;
	EndFunc ;
QueueItem.____SetNext:
	BeginFunc 0 ;
	*(this + 8) = n ;
	EndFunc ;
QueueItem.____SetPrev:
	BeginFunc 0 ;
	*(this + 12) = p ;
	EndFunc ;
VTable QueueItem =
	QueueItem.____Init,
	QueueItem.____GetData,
	QueueItem.____GetNext,
	QueueItem.____GetPrev,
	QueueItem.____SetNext,
	QueueItem.____SetPrev,
; 
Queue.____Init:
	BeginFunc 44 ;
	_tmp3 = 12 ;
	_tmp4 = 4 ;
	_tmp5 = _tmp4 + _tmp3 ;
	PushParam _tmp5 ;
	_tmp6 = LCall _Alloc ;
	PopParams 4 ;
	_tmp7 = QueueItem ;
	*(_tmp6) = _tmp7 ;
	*(this + 4) = _tmp6 ;
	_tmp8 = 0 ;
	_tmp9 = *(this + 4) ;
	_tmp10 = *(this + 4) ;
	PushParam _tmp10 ;
	PushParam _tmp9 ;
	PushParam _tmp8 ;
	_tmp11 = *(this + 4) ;
	PushParam _tmp11 ;
	_tmp12 = *(_tmp11) ;
	_tmp13 = *(_tmp12) ;
	ACall _tmp13 ;
	PopParams 16 ;
	EndFunc ;
Queue.____EnQueue:
	BeginFunc 56 ;
	_tmp14 = 12 ;
	_tmp15 = 4 ;
	_tmp16 = _tmp15 + _tmp14 ;
	PushParam _tmp16 ;
	_tmp17 = LCall _Alloc ;
	PopParams 4 ;
	_tmp18 = QueueItem ;
	*(_tmp17) = _tmp18 ;
	temp = _tmp17 ;
	_tmp19 = *(this + 4) ;
	PushParam _tmp19 ;
	_tmp20 = *(_tmp19) ;
	_tmp21 = *(_tmp20 + 8) ;
	_tmp22 = ACall _tmp21 ;
	PopParams 4 ;
	_tmp23 = *(this + 4) ;
	PushParam _tmp23 ;
	PushParam _tmp22 ;
	PushParam i ;
	PushParam temp ;
	_tmp24 = *(temp) ;
	_tmp25 = *(_tmp24) ;
	ACall _tmp25 ;
	PopParams 16 ;
	EndFunc ;
Queue.____DeQueue:
	BeginFunc 156 ;
	_tmp26 = *(this + 4) ;
	PushParam _tmp26 ;
	_tmp27 = *(_tmp26) ;
	_tmp28 = *(_tmp27 + 12) ;
	_tmp29 = ACall _tmp28 ;
	PopParams 4 ;
	_tmp30 = *(this + 4) ;
	_tmp31 = _tmp29 == _tmp30 ;
	IfZ _tmp31 Goto _L0 ;
	_tmp32 = "Queue Is Empty" ;
	PushParam _tmp32 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp33 = 0 ;
	Return _tmp33 ;
	Goto _L1 ;
_L0:
	_tmp34 = *(this + 4) ;
	PushParam _tmp34 ;
	_tmp35 = *(_tmp34) ;
	_tmp36 = *(_tmp35 + 12) ;
	_tmp37 = ACall _tmp36 ;
	PopParams 4 ;
	temp = _tmp37 ;
	PushParam temp ;
	_tmp38 = *(temp) ;
	_tmp39 = *(_tmp38 + 4) ;
	_tmp40 = ACall _tmp39 ;
	PopParams 4 ;
	val = _tmp40 ;
	PushParam temp ;
	_tmp41 = *(temp) ;
	_tmp42 = *(_tmp41 + 8) ;
	_tmp43 = ACall _tmp42 ;
	PopParams 4 ;
	PushParam _tmp43 ;
	PushParam temp ;
	_tmp44 = *(temp) ;
	_tmp45 = *(_tmp44 + 12) ;
	_tmp46 = ACall _tmp45 ;
	PopParams 4 ;
	PushParam _tmp46 ;
	_tmp47 = *(_tmp46) ;
	_tmp48 = *(_tmp47 + 16) ;
	ACall _tmp48 ;
	PopParams 8 ;
	PushParam temp ;
	_tmp49 = *(temp) ;
	_tmp50 = *(_tmp49 + 12) ;
	_tmp51 = ACall _tmp50 ;
	PopParams 4 ;
	PushParam _tmp51 ;
	PushParam temp ;
	_tmp52 = *(temp) ;
	_tmp53 = *(_tmp52 + 8) ;
	_tmp54 = ACall _tmp53 ;
	PopParams 4 ;
	PushParam _tmp54 ;
	_tmp55 = *(_tmp54) ;
	_tmp56 = *(_tmp55 + 20) ;
	ACall _tmp56 ;
	PopParams 8 ;
_L1:
	Return val ;
	EndFunc ;
VTable Queue =
	Queue.____Init,
	Queue.____EnQueue,
	Queue.____DeQueue,
; 
main:
	BeginFunc 280 ;
	_tmp57 = 4 ;
	_tmp58 = 4 ;
	_tmp59 = _tmp58 + _tmp57 ;
	PushParam _tmp59 ;
	_tmp60 = LCall _Alloc ;
	PopParams 4 ;
	_tmp61 = Queue ;
	*(_tmp60) = _tmp61 ;
	q = _tmp60 ;
	PushParam q ;
	_tmp62 = *(q) ;
	_tmp63 = *(_tmp62) ;
	ACall _tmp63 ;
	PopParams 4 ;
	_tmp64 = 0 ;
	i = _tmp64 ;
_L2:
	_tmp66 = 10 ;
	_tmp67 = i == _tmp66 ;
	IfZ _tmp67 Goto _L5 ;
	_tmp68 = 0 ;
	_tmp65 = _tmp68 ;
	Goto _L4 ;
_L5:
	_tmp69 = 1 ;
	_tmp65 = _tmp69 ;
_L4:
	IfZ _tmp65 Goto _L3 ;
	PushParam i ;
	PushParam q ;
	_tmp70 = *(q) ;
	_tmp71 = *(_tmp70 + 4) ;
	ACall _tmp71 ;
	PopParams 8 ;
	_tmp72 = 1 ;
	_tmp73 = i + _tmp72 ;
	i = _tmp73 ;
	Goto _L2 ;
_L3:
	_tmp74 = 0 ;
	i = _tmp74 ;
_L6:
	_tmp76 = 4 ;
	_tmp77 = i == _tmp76 ;
	IfZ _tmp77 Goto _L9 ;
	_tmp78 = 0 ;
	_tmp75 = _tmp78 ;
	Goto _L8 ;
_L9:
	_tmp79 = 1 ;
	_tmp75 = _tmp79 ;
_L8:
	IfZ _tmp75 Goto _L7 ;
	PushParam q ;
	_tmp80 = *(q) ;
	_tmp81 = *(_tmp80 + 8) ;
	_tmp82 = ACall _tmp81 ;
	PopParams 4 ;
	PushParam _tmp82 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp83 = " " ;
	PushParam _tmp83 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp84 = 1 ;
	_tmp85 = i + _tmp84 ;
	i = _tmp85 ;
	Goto _L6 ;
_L7:
	_tmp86 = "\n" ;
	PushParam _tmp86 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp87 = 0 ;
	i = _tmp87 ;
_L10:
	_tmp89 = 10 ;
	_tmp90 = i == _tmp89 ;
	IfZ _tmp90 Goto _L13 ;
	_tmp91 = 0 ;
	_tmp88 = _tmp91 ;
	Goto _L12 ;
_L13:
	_tmp92 = 1 ;
	_tmp88 = _tmp92 ;
_L12:
	IfZ _tmp88 Goto _L11 ;
	PushParam i ;
	PushParam q ;
	_tmp93 = *(q) ;
	_tmp94 = *(_tmp93 + 4) ;
	ACall _tmp94 ;
	PopParams 8 ;
	_tmp95 = 1 ;
	_tmp96 = i + _tmp95 ;
	i = _tmp96 ;
	Goto _L10 ;
_L11:
	_tmp97 = 0 ;
	i = _tmp97 ;
_L14:
	_tmp99 = 17 ;
	_tmp100 = i == _tmp99 ;
	IfZ _tmp100 Goto _L17 ;
	_tmp101 = 0 ;
	_tmp98 = _tmp101 ;
	Goto _L16 ;
_L17:
	_tmp102 = 1 ;
	_tmp98 = _tmp102 ;
_L16:
	IfZ _tmp98 Goto _L15 ;
	PushParam q ;
	_tmp103 = *(q) ;
	_tmp104 = *(_tmp103 + 8) ;
	_tmp105 = ACall _tmp104 ;
	PopParams 4 ;
	PushParam _tmp105 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp106 = " " ;
	PushParam _tmp106 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp107 = 1 ;
	_tmp108 = i + _tmp107 ;
	i = _tmp108 ;
	Goto _L14 ;
_L15:
	_tmp109 = "\n" ;
	PushParam _tmp109 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
